package com.ooooonly.lma.logger.impl

import com.ooooonly.lma.datastore.entity.LogItem
import com.ooooonly.lma.logger.LmaLogger
import com.ooooonly.lma.logger.LogRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LmaLoggerImpl @Inject constructor(
    private val logRepository: LogRepository
): LmaLogger {
    override suspend fun log(from: Int, level: Int, identity: String, content: String) {
        val item = LogItem(
            from = from,
            level = level,
            identity = identity,
            content = content
        )
        logRepository.saveLogs(item)
    }
}