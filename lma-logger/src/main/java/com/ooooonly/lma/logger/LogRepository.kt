package com.ooooonly.lma.logger

import com.ooooonly.lma.datastore.entity.LogItem
import kotlinx.coroutines.flow.Flow

interface LogRepository {
    fun getLogs(config: LogQueryConfig): Flow<List<LogItem>>

    suspend fun saveLogs(vararg logs: LogItem)

    suspend fun removeLogs(vararg logs: LogItem)
}