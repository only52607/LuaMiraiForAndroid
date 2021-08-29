package com.ooooonly.lma.log

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.sqlite.db.SimpleSQLiteQuery
import com.ooooonly.lma.data.dao.LogDao
import com.ooooonly.lma.model.entity.LogEntity
import com.ooooonly.lma.ui.log.LogState
import com.ooooonly.lma.utils.getIntSafe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class LogViewModel @Inject constructor(
    private val logDao: LogDao,
    private val preferences: SharedPreferences
):CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob()

    private var logBuffer = mutableListOf<LogEntity>()
    private val _logs = mutableStateOf<List<LogEntity>>(listOf())
    val logs:List<LogEntity> by _logs

    private var _initialized by mutableStateOf(false)
    val initialized: Boolean = _initialized

    val logState = LogState()

    private val querySize = preferences.getIntSafe("log_buffer_size",200)
    private val triggerLimit = preferences.getIntSafe("log_limit_size",300)
    private val reduceSize = preferences.getIntSafe("log_reduce_size",50)

    init {
        launch {
            _initialized = true
            collectLogState()
        }
    }

    private suspend fun collectLogState() {
        snapshotFlow {
            val showBotMessageLog = logState.filterState.showBotMessageLog
            val showBotNetLog = logState.filterState.showBotNetLog
            val showScriptOutput = logState.filterState.showScriptOutput
            val conditions = mutableListOf<String>()
            if (showBotMessageLog) {
                conditions.add("`from`=${LogEntity.FROM_BOT_PRIMARY}")
            }
            if (showBotNetLog) {
                conditions.add("`from`=${LogEntity.FROM_BOT_NETWORK}")
            }
            if (showScriptOutput) {
                conditions.add("`from`=${LogEntity.FROM_SCRIPT}")
            }
            if (conditions.isEmpty()) {
                return@snapshotFlow null
            }
            val conditionString = "where ${conditions.joinToString(separator = " or ")}"
            val queryStringBuilder = StringBuilder("SELECT * FROM log")
            queryStringBuilder.append(" $conditionString order by id desc limit ?")
            SimpleSQLiteQuery(queryStringBuilder.toString(), arrayOf(querySize))
        }.collect { query ->
            if (query == null) {
                clearBuffer()
                return@collect
            }
            logDao.getLogs(query).apply { reverse() }.let {
                synchronized(logBuffer) {
                    logBuffer = it.toMutableList()
                    _logs.value = logBuffer
                }
            }
        }
    }

    private fun matchState(logEntity: LogEntity):Boolean {
        if (logState.filterState.showBotMessageLog && logEntity.from == LogEntity.FROM_BOT_PRIMARY)
            return true
        if (logState.filterState.showBotNetLog && logEntity.from == LogEntity.FROM_BOT_NETWORK)
            return true
        if (logState.filterState.showScriptOutput && logEntity.from == LogEntity.FROM_SCRIPT)
            return true
        return false
    }

    private fun clearBuffer() {
        logBuffer = mutableListOf()
        _logs.value = logBuffer
    }

    private fun reduceBuffer() {
        if (logBuffer.size >= triggerLimit) {
            logBuffer = logBuffer.toTypedArray().copyOfRange(reduceSize, logBuffer.size - 1).toMutableList()
        }
    }

    fun insertLog(logEntity: LogEntity) {
        launch {
            logEntity.id = logDao.saveLog(logEntity)
        }
        if (!matchState(logEntity)) return
        synchronized(logBuffer) {
            logBuffer.add(logEntity)
            _logs.value = logBuffer
            reduceBuffer()
        }
    }

    fun clearLog() {
        launch {
            clearBuffer()
            logDao.deleteAllLog()
        }
    }
}