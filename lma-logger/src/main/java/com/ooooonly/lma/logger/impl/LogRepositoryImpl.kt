package com.ooooonly.lma.logger.impl

import androidx.sqlite.db.SimpleSQLiteQuery
import com.ooooonly.lma.datastore.dao.LogDao
import com.ooooonly.lma.datastore.entity.LogItem
import com.ooooonly.lma.logger.LogQueryConfig
import com.ooooonly.lma.logger.LogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepositoryImpl @Inject constructor(
    private val logDao: LogDao
): LogRepository {
    override fun getLogs(config: LogQueryConfig): Flow<List<LogItem>> {
        val conditions = mutableListOf("false")
        if (config.containBotMessageLog) {
            conditions.add("`from`=${LogItem.FROM_BOT_PRIMARY}")
        }
        if (config.containBotNetLog) {
            conditions.add("`from`=${LogItem.FROM_BOT_NETWORK}")
        }
        if (config.containScriptOutput) {
            conditions.add("`from`=${LogItem.FROM_SCRIPT}")
        }
        if (config.containMclLog) {
            conditions.add("`from`=${LogItem.FROM_MCL}")
        }
        val query = SimpleSQLiteQuery("""
            SELECT * FROM log 
            where ${conditions.joinToString(separator = " or ")}
            order by id desc limit ?
        """, arrayOf(config.limits))
        return logDao.getLogs(query).distinctUntilChanged()
    }

    override suspend fun saveLogs(vararg logs: LogItem) {
        logs.forEach { log ->
            log.id = logDao.saveLog(log)
        }
    }

    override suspend fun removeLogs(vararg logs: LogItem) {
        logDao.deleteLogs(*logs)
    }
}