package com.ooooonly.lma.logger.utils.impl

import com.ooooonly.lma.logger.LogLevel
import com.ooooonly.lma.logger.LogType
import com.ooooonly.lma.logger.repository.LogRepository
import com.ooooonly.lma.logger.utils.LmaLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LmaLoggerImpl @Inject constructor(
    private val logRepository: LogRepository
): LmaLogger {
    override suspend fun log(type: LogType, level: LogLevel, identity: String, content: String) {
        logRepository.addLog(type, level, identity, content)
    }
}