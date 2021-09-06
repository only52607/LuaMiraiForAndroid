package com.ooooonly.lma.script

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ooooonly.lma.data.dao.ScriptDao
import com.ooooonly.lma.log.LogViewModel
import com.ooooonly.lma.model.AppFiles
import com.ooooonly.lma.model.entity.LogEntity
import com.ooooonly.lma.model.entity.ScriptEntity
import com.ooooonly.luaMirai.base.ScriptLang
import kotlinx.coroutines.*
import net.mamoe.mirai.utils.MiraiInternalApi
import java.io.File
import java.net.URL
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@OptIn(MiraiInternalApi::class)
@Singleton
class ScriptViewModel @Inject constructor(
    private val scriptDao: ScriptDao,
    private val appFiles: AppFiles,
    private val logViewModel: LogViewModel,
    private val scriptBuilder: ScriptBuilder,
    private val contentResolver: ContentResolver
) : CoroutineScope {
    private val scriptDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    override val coroutineContext: CoroutineContext = SupervisorJob()

    private val _scripts = mutableStateListOf<ScriptState>()
    val scripts: List<ScriptState> = _scripts

    private var _initialized by mutableStateOf(false)
    val initialized: Boolean = _initialized

    init {
        launch {
            createScriptStatesFromRepository()
            _initialized = true
        }
    }

    private fun ScriptState.error(cause: Throwable) {
        val phase = this@error.phase
        if (phase is BotScriptInstanceOwner) {
            logViewModel.insertLog(
                LogEntity(
                    from = LogEntity.FROM_SCRIPT,
                    level = LogEntity.LEVEL_ERROR,
                    content = cause.stackTraceToString(),
                    identity = phase.instance.header["name"] ?: ""
                )
            )
        } else {
            logViewModel.insertLog(
                LogEntity(
                    from = LogEntity.FROM_SCRIPT,
                    level = LogEntity.LEVEL_ERROR,
                    content = cause.stackTraceToString(),
                    identity = "Script: ${entity.id}"
                )
            )
        }
    }

    private fun ScriptState.enable() {
        val phase = this.phase
        if (phase !is BotScriptInstanceOwner) return
        val job = this@ScriptViewModel.launch(start = CoroutineStart.LAZY) {
            try {
                withContext(scriptDispatcher) {
                    phase.instance.load()
                }
                this@enable.phase = ScriptPhase.Enabled(phase.instance)
            } catch (e: Exception) {
                Log.e("script", "load script failed", e)
                this@enable.error(e)
                this@enable.phase = ScriptPhase.FailedOnLoading(phase.instance, e)
            }
        }
        this.phase = ScriptPhase.Loading(phase.instance, job)
        job.start()
    }

    private fun ScriptState.disable() {
        val phase = this@disable.phase
        if (phase is JobOwner) {
            kotlin.runCatching {
                phase.job.cancel()
            }
        }
        if (phase !is BotScriptInstanceOwner) return
        if (!phase.instance.isLoaded) return
        launch {
            withContext(scriptDispatcher) {
                phase.instance.stop()
            }
            this@disable.phase = ScriptPhase.Disabled(phase.instance)
        }
    }

    private fun ScriptState.update() {
        val phase = this.phase
        if (phase !is BotScriptInstanceOwner) return
        val job = launch(start = CoroutineStart.LAZY) {
            withContext(scriptDispatcher) {
                try {
                    phase.instance.reload()
                    Log.d("script", "reload successful")
                    if (phase.instance.isLoaded) {
                        this@update.phase = ScriptPhase.Enabled(phase.instance)
                    } else {
                        this@update.phase = ScriptPhase.Disabled(phase.instance)
                    }
                } catch (e: Exception) {
                    this@update.error(e)
                    this@update.phase = ScriptPhase.FailedOnUpdating(phase.instance, e)
                }
            }
        }
        this@update.phase = ScriptPhase.Updating(phase.instance, job)
        Log.d("script", "reload start")
        job.start()
    }

    private fun ScriptEntity.clear() {
        if (this.type == ScriptEntity.TYPE_FILE) {
            val file = File(this.source)
            if (file.exists() && file.canonicalPath.startsWith(appFiles.scriptDirectory.canonicalPath)) {
                file.delete()
            }
        }
    }

    private fun createScriptState(entity: ScriptEntity, prepare: (suspend () -> Unit)? = null): ScriptState {
        return ScriptState(entity).also { state ->
            _scripts.add(state)
            val creatingJob = launch(start = CoroutineStart.LAZY) {
                withContext(Dispatchers.Default) {
                    val instance = try {
                        prepare?.invoke()
                        scriptBuilder.buildBotScript(entity)
                    } catch (e: Exception) {
                        state.error(e)
                        Log.e("script", "failed on creating", e)
                        state.phase = ScriptPhase.FailedOnCreating(e)
                        return@withContext
                    }
                    state.phase = ScriptPhase.Disabled(instance)
                    if (state.entity.enabled) {
                        state.enable()
                    }
                }
            }
            state.phase = ScriptPhase.Creating(creatingJob)
            creatingJob.start()
        }
    }

    private suspend fun createScriptStatesFromRepository() = withContext(scriptDispatcher) {
        scriptDao.loadAllScripts().forEach { entity ->
            Log.d("script", "read $entity")
            createScriptState(entity)
        }
    }

    fun addScript(file: File, lang: ScriptLang = ScriptLang.Lua) {
        val entity = ScriptEntity(
            source = file.path,
            type = ScriptEntity.TYPE_FILE,
            enabled = false,
            lang = lang
        )
        createScriptState(entity)
        launch {
            entity.id = scriptDao.saveScript(entity)
        }
    }

    fun addScript(url: URL, lang: ScriptLang = ScriptLang.Lua) {
        val entity = ScriptEntity(
            source = url.toString(),
            type = ScriptEntity.TYPE_URL,
            enabled = false,
            lang = lang
        )
        createScriptState(entity)
        launch {
            entity.id = scriptDao.saveScript(entity)
        }
    }

    fun addScript(uri: Uri?, lang: ScriptLang = ScriptLang.Lua) {
        if (uri == null) return
        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        when (uri.scheme) {
            ContentResolver.SCHEME_FILE -> addScript(File(uri.path!!))
            else -> {
                val tempFile = File(appFiles.scriptDirectory, System.currentTimeMillis().toString())
                val entity = ScriptEntity(
                    source = tempFile.path,
                    type = ScriptEntity.TYPE_FILE,
                    enabled = false,
                    lang = lang
                )
                createScriptState(entity) {
                    withContext(Dispatchers.IO) {
                        contentResolver.openInputStream(uri).use { input ->
                            tempFile.outputStream().use { output ->
                                input?.copyTo(output)
                            }
                        }
                    }
                }
                launch {
                    entity.id = scriptDao.saveScript(entity)
                }
            }
        }
    }

    fun enableScript(scriptState: ScriptState) {
        scriptState.enable()
        Log.d("script", "enable " + scriptState.entity.id.toString())
        launch {
            scriptState.entity.enabled = true
            scriptDao.saveScript(scriptState.entity)
        }
    }

    fun disableScript(scriptState: ScriptState) {
        scriptState.disable()
        launch {
            scriptState.entity.enabled = false
            scriptDao.saveScript(scriptState.entity)
        }
    }

    fun deleteScript(scriptState: ScriptState) {
        scriptState.disable()
        _scripts.remove(scriptState)
        launch {
            scriptState.entity.clear()
            scriptDao.deleteScript(scriptState.entity)
        }
    }

    fun updateScript(scriptState: ScriptState) {
        scriptState.update()
    }
}