package com.ooooonly.lma.script.impl

import android.util.Log
import com.ooooonly.lma.datastore.dao.ScriptDao
import com.ooooonly.lma.datastore.entity.LogItem
import com.ooooonly.lma.datastore.entity.ScriptItem
import com.ooooonly.lma.script.ScriptBuilder
import com.ooooonly.lma.utils.outputStreamOf
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintStream
import java.lang.RuntimeException
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ScriptBuilderImpl @Inject constructor(
    val logViewModel: LogViewModel,
    val scriptDao: ScriptDao
) : ScriptBuilder {
    private suspend fun String.toURL(): URL = withContext(Dispatchers.IO) {
        return@withContext URL(this@toURL)
    }

    private fun scriptStdPrintStreamBuilder(script: BotScript): PrintStream {
        return PrintStream(outputStreamOf {
            Log.d("script", it)
            logViewModel.insertLog(
                LogItem(
                    from = LogItem.FROM_SCRIPT,
                    level = LogItem.LEVEL_INFO,
                    content = it,
                    identity = script.header["name"] ?: ""
                )
            )
        }, true)
    }

    private fun scriptErrPrintStreamBuilder(script: BotScript): PrintStream {
        return PrintStream(outputStreamOf {
            Log.d("script", it)
            logViewModel.insertLog(
                LogItem(
                    from = LogItem.FROM_SCRIPT,
                    level = LogItem.LEVEL_ERROR,
                    content = it,
                    identity = script.header["name"] ?: ""
                )
            )
        }, true)
    }

    private fun scriptCoroutineContext(script: BotScript): CoroutineContext {
        return CoroutineExceptionHandler { coroutineContext, throwable ->
            logViewModel.insertLog(
                LogItem(
                    from = LogItem.FROM_SCRIPT,
                    level = LogItem.LEVEL_ERROR,
                    content = throwable.stackTraceToString(),
                    identity = script.header["name"] ?: ""
                )
            )
        }
    }

    override suspend fun buildBotScript(item: ScriptItem): BotScript =
        when (item.type) {
            ScriptItem.TYPE_FILE -> {
                val file = File(item.source)
                if (!file.exists()) throw FileNotFoundException("找不到文件")
                BotScriptFactory.buildBotScript(
                    item.lang,
                    file,
                    stdout = ::scriptStdPrintStreamBuilder,
                    stderr = ::scriptErrPrintStreamBuilder,
                    extraCoroutineContext = ::scriptCoroutineContext
                )
            }
            ScriptItem.TYPE_URL -> BotScriptFactory.buildBotScript(
                item.lang,
                item.source.toURL(),
                stdout = ::scriptStdPrintStreamBuilder,
                stderr = ::scriptErrPrintStreamBuilder,
                extraCoroutineContext = ::scriptCoroutineContext
            )
            ScriptItem.TYPE_CONTENT -> BotScriptFactory.buildBotScript(
                item.lang,
                item.source,
                stdout = ::scriptStdPrintStreamBuilder,
                stderr = ::scriptErrPrintStreamBuilder,
                extraCoroutineContext = ::scriptCoroutineContext
            )
            else -> throw UnknownScriptTypeException("未知的脚本类型")
        }
}

class UnknownScriptTypeException(message: String) : RuntimeException(message)