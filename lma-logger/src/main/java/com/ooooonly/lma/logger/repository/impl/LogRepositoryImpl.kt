package com.ooooonly.lma.logger.repository.impl

import androidx.sqlite.db.SimpleSQLiteQuery
import com.ooooonly.lma.datastore.dao.LogDao
import com.ooooonly.lma.datastore.entity.LogEntity
import com.ooooonly.lma.logger.LogLevel
import com.ooooonly.lma.logger.LogType
import com.ooooonly.lma.logger.repository.Log
import com.ooooonly.lma.logger.repository.LogChangeListener
import com.ooooonly.lma.logger.repository.LogRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LogRepositoryImpl @Inject constructor(
    private val logDao: LogDao
) : LogRepository {
    private val Log.entityValue: LogEntity
        get() = LogEntity(
            id = id,
            from = when (type) {
                LogType.Script -> LogEntity.FROM_SCRIPT
                LogType.MiraiConsole -> LogEntity.FROM_MCL
                LogType.Network -> LogEntity.FROM_BOT_NETWORK
                LogType.Bot -> LogEntity.FROM_BOT_PRIMARY
            },
            level = when (level) {
                LogLevel.Info -> LogEntity.LEVEL_INFO
                LogLevel.Debug -> LogEntity.LEVEL_DEBUG
                LogLevel.Error -> LogEntity.LEVEL_ERROR
                LogLevel.Warning -> LogEntity.LEVEL_WARNING
                LogLevel.Verbose -> LogEntity.LEVEL_VERBOSE
            },
            identity = identity,
            content = content,
            date = date
        )

    private val LogEntity.logValue
        get() = Log(
            id = id,
            type = when (from) {
                LogEntity.FROM_SCRIPT -> LogType.Script
                LogEntity.FROM_MCL -> LogType.MiraiConsole
                LogEntity.FROM_BOT_NETWORK -> LogType.Network
                LogEntity.FROM_BOT_PRIMARY -> LogType.Bot
                else -> throw Exception("Unknown log type $from")
            },
            level = when (level) {
                LogEntity.LEVEL_INFO -> LogLevel.Info
                LogEntity.LEVEL_DEBUG -> LogLevel.Debug
                LogEntity.LEVEL_ERROR -> LogLevel.Error
                LogEntity.LEVEL_WARNING -> LogLevel.Warning
                LogEntity.LEVEL_VERBOSE -> LogLevel.Verbose
                else -> throw Exception("Unknown log level $from")
            },
            identity = identity,
            content = content,
            date = date
        )

    private val listeners = mutableSetOf<LogChangeListener>()

    override suspend fun loadLogs(
        containBotMessageLog: Boolean,
        containBotNetLog: Boolean,
        containScriptOutput: Boolean,
        containMclLog: Boolean,
        limits: Long
    ): List<Log> {
        val conditions = mutableListOf("false")
        if (containBotMessageLog) {
            conditions.add("`from`=${LogEntity.FROM_BOT_PRIMARY}")
        }
        if (containBotNetLog) {
            conditions.add("`from`=${LogEntity.FROM_BOT_NETWORK}")
        }
        if (containScriptOutput) {
            conditions.add("`from`=${LogEntity.FROM_SCRIPT}")
        }
        if (containMclLog) {
            conditions.add("`from`=${LogEntity.FROM_MCL}")
        }
        val query = SimpleSQLiteQuery(
            """
            SELECT * FROM log 
            where ${conditions.joinToString(separator = " or ")}
            order by id desc limit ?
        """, arrayOf(limits)
        )
        return logDao.selectLogs(query).map { it.logValue }
    }

    override suspend fun loadLogsBefore(log: Log, limit: Int): List<Log> {
        return logDao.selectLogsBefore(log.id!!, limit).map { it.logValue }
    }

    override suspend fun saveLogs(vararg logs: Log) {
        logDao.saveLogs(logs.map { it.entityValue })
        listeners.forEach { it.onInsert(*logs) }
    }

    override suspend fun removeAllLogs() {
        logDao.deleteAllLog()
        listeners.forEach { it.onClear() }
    }

    override fun addListener(listener: LogChangeListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: LogChangeListener) {
        listeners.remove(listener)
    }
}