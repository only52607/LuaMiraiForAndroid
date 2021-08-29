package com.ooooonly.lma.script

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ooooonly.lma.data.dao.ScriptDao
import com.ooooonly.lma.model.AppFiles
import com.ooooonly.lma.model.entity.LogEntity
import com.ooooonly.lma.model.entity.ScriptEntity
import com.ooooonly.lma.log.LogViewModel
import com.ooooonly.luaMirai.base.*
import kotlinx.coroutines.*
import net.mamoe.mirai.utils.MiraiInternalApi
import java.io.File
import java.io.InputStream
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
    private val scriptBuilder: ScriptBuilder
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

    private suspend fun ScriptState.loadSafety() {
        loading = true
        enabled = true
        try {
            instance.load()
        } catch (e: Exception) {
            Log.e("script", "load script failed", e)
            logViewModel.insertLog(
                LogEntity(
                    from = LogEntity.FROM_SCRIPT,
                    level = LogEntity.LEVEL_ERROR,
                    content = e.stackTraceToString(),
                    identity = instance.header["name"] ?: ""
                )
            )
            enabled = false
        }
        loading = false
    }

    private fun ScriptState.enable() {
        this.job = this@ScriptViewModel.launch {
            loadSafety()
        }
    }

    private suspend fun ScriptState.disable() {
        if (!enabled || !instance.isLoaded) return
        job?.cancel()
        job = null
        instance.stop()
        enabled = false
        entity.enabled = false
    }

    private suspend fun ScriptState.remove() {
        disable()
        val source = instance.source
        if (source is BotScriptFileSource) {
            source.file.delete()
        }
        _scripts.remove(this)
    }

    private suspend fun createScriptState(entity: ScriptEntity): ScriptState? {
        return scriptBuilder.buildBotScript(entity)?.let { botScript ->
            ScriptState(botScript, entity).also { state ->
                _scripts.add(state)
                if (entity.enabled) state.enable()
            }
        }
    }

    private suspend fun createScriptStatesFromRepository() = withContext(scriptDispatcher) {
        scriptDao.loadAllScripts().forEach { entity ->
            Log.d("script", "read $entity")
            createScriptState(entity)
        }
    }

    suspend fun addScript(file: File, lang: ScriptLang = ScriptLang.Lua) {
        val entity = ScriptEntity(
            source = file.path,
            type = ScriptEntity.TYPE_FILE,
            enabled = false,
            lang = lang
        )
        createScriptState(entity)
        entity.id = scriptDao.saveScript(entity)
    }

    suspend fun addScript(inputStream: InputStream, name: String = "Script_${System.currentTimeMillis()}") {
        val scriptFile = File(appFiles.scriptDirectory, name)
        withContext(Dispatchers.IO) {
            inputStream.use { input ->
                scriptFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        addScript(scriptFile)
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
        launch {
            scriptState.disable()
        }
        launch {
            scriptDao.saveScript(scriptState.entity)
        }
    }

    fun deleteScript(scriptState: ScriptState) {
        launch(Dispatchers.Main) {
            scriptState.remove()
        }
        launch {
            scriptDao.deleteScript(scriptState.entity)
        }
    }
}
