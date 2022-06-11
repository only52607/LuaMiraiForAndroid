package com.ooooonly.lma.logger.repository

interface LogRepository {
    suspend fun loadLogs(
        containBotMessageLog: Boolean,
        containBotNetLog: Boolean,
        containScriptOutput: Boolean,
        containMclLog: Boolean,
        limits: Long = 200
    ): List<Log>

    suspend fun loadLogsBefore(log: Log, limit: Int): List<Log>

    suspend fun saveLogs(vararg logs: Log)

    suspend fun removeAllLogs()

    fun addListener(listener: LogChangeListener)

    fun removeListener(listener: LogChangeListener)
}

