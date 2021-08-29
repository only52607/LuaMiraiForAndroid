package com.ooooonly.lma.script.impl

import android.util.Log
import com.ooooonly.lma.data.dao.ScriptDao
import com.ooooonly.lma.model.entity.LogEntity
import com.ooooonly.lma.model.entity.ScriptEntity
import com.ooooonly.lma.script.ScriptBuilder
import com.ooooonly.lma.log.LogViewModel
import com.ooooonly.lma.utils.outputStreamOf
import com.ooooonly.luaMirai.base.BotScript
import com.ooooonly.luaMirai.base.BotScriptFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.PrintStream
import java.net.URL
import javax.inject.Inject

class ScriptBuilderImpl @Inject constructor(
    val logViewModel: LogViewModel,
    val scriptDao: ScriptDao
) : ScriptBuilder {
    private suspend fun String.toURL(): URL = withContext(Dispatchers.IO) {
        return@withContext URL(this@toURL)
    }

    private val scriptStdPrintStreamBuilder: (BotScript) -> PrintStream = { script ->
        PrintStream(outputStreamOf {
            Log.d("script", it)
            logViewModel.insertLog(
                LogEntity(
                    from = LogEntity.FROM_SCRIPT,
                    level = LogEntity.LEVEL_INFO,
                    content = it,
                    identity = script.header["name"] ?: ""
                )
            )
        }, true)
    }

    private val scriptErrPrintStreamBuilder: (BotScript) -> PrintStream = { script ->
        PrintStream(outputStreamOf {
            Log.d("script", it)
            logViewModel.insertLog(
                LogEntity(
                    from = LogEntity.FROM_SCRIPT,
                    level = LogEntity.LEVEL_ERROR,
                    content = it,
                    identity = script.header["name"] ?: ""
                )
            )
        }, true)
    }

    override suspend fun buildBotScript(entity: ScriptEntity): BotScript? = try {
        when (entity.type) {
            ScriptEntity.TYPE_FILE -> {
                val file = File(entity.source)
                if (!file.exists()) {
                    scriptDao.deleteScript(entity)
                    null
                } else {
                    BotScriptFactory.buildBotScript(
                        entity.lang,
                        file,
                        stdout = scriptStdPrintStreamBuilder,
                        stderr = scriptErrPrintStreamBuilder
                    )
                }
            }
            ScriptEntity.TYPE_URL -> BotScriptFactory.buildBotScript(
                entity.lang,
                entity.source.toURL(),
                stdout = scriptStdPrintStreamBuilder,
                stderr = scriptErrPrintStreamBuilder
            )
            ScriptEntity.TYPE_CONTENT -> BotScriptFactory.buildBotScript(
                entity.lang,
                entity.source,
                stdout = scriptStdPrintStreamBuilder,
                stderr = scriptErrPrintStreamBuilder
            )
            else -> null
        }
    } catch (e: Exception) {
        Log.e("script", "load script failed.", e)
        null
    }
}