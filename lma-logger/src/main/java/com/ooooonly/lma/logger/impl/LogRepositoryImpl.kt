package com.ooooonly.lma.logger.impl

import androidx.sqlite.db.SimpleSQLiteQuery
import com.ooooonly.lma.datastore.dao.LogDao
import com.ooooonly.lma.datastore.entity.LogItem
import com.ooooonly.lma.logger.LogInsertListener
import com.ooooonly.lma.logger.LogRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepositoryImpl @Inject constructor(
    private val logDao: LogDao
) : LogRepository {
    private val listeners = mutableSetOf<LogInsertListener>()

    override suspend fun loadLogs(
        containBotMessageLog: Boolean,
        containBotNetLog: Boolean,
        containScriptOutput: Boolean,
        containMclLog: Boolean,
        limits: Long
    ): List<LogItem> {
        val conditions = mutableListOf("false")
        if (containBotMessageLog) {
            conditions.add("`from`=${LogItem.FROM_BOT_PRIMARY}")
        }
        if (containBotNetLog) {
            conditions.add("`from`=${LogItem.FROM_BOT_NETWORK}")
        }
        if (containScriptOutput) {
            conditions.add("`from`=${LogItem.FROM_SCRIPT}")
        }
        if (containMclLog) {
            conditions.add("`from`=${LogItem.FROM_MCL}")
        }
        val query = SimpleSQLiteQuery(
            """
            SELECT * FROM log 
            where ${conditions.joinToString(separator = " or ")}
            order by id desc limit ?
        """, arrayOf(limits)
        )
        return logDao.loadLogs(query)
    }

    override suspend fun loadLogsBefore(logItem: LogItem, limit: Int): List<LogItem> {
        return logDao.loadLogsBefore(logItem.id!!, limit)
    }

    override suspend fun saveLogs(vararg logs: LogItem) {
        logs.forEach { log ->
            log.id = logDao.saveLog(log)
            listeners.forEach { it.onInsert(log) }
        }
    }

    override suspend fun removeAllLogs() {
        logDao.deleteAllLog()
    }

    override fun addLogInsertListener(listener: LogInsertListener) {
        listeners.add(listener)
    }

    override fun removeLogInsertListener(listener: LogInsertListener) {
        listeners.remove(listener)
    }
}