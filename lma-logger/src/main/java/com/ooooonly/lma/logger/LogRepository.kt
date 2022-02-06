package com.ooooonly.lma.logger

import com.ooooonly.lma.datastore.entity.LogItem

interface LogRepository {
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

    fun addLogInsertListener(listener: LogInsertListener)

    fun removeLogInsertListener(listener: LogInsertListener)
}

fun interface LogInsertListener {
    fun onInsert(log: LogItem): Unit
}