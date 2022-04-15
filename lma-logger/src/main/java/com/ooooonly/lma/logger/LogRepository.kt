package com.ooooonly.lma.logger

import com.ooooonly.lma.datastore.entity.LogItem
import kotlinx.coroutines.flow.SharedFlow

interface LogRepository {
    val logFlow: SharedFlow<LogItem>

    suspend fun loadLogs(
        containBotMessageLog: Boolean,
        containBotNetLog: Boolean,
        containScriptOutput: Boolean,
        containMclLog: Boolean,
        limits: Long = 200
    ): List<LogItem>

    suspend fun loadLogsBefore(logItem: LogItem, limit: Int): List<LogItem>

    suspend fun saveLogs(vararg logs: LogItem)

    suspend fun removeAllLogs()
}