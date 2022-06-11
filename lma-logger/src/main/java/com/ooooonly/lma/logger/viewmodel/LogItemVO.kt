package com.ooooonly.lma.logger.viewmodel

import com.ooooonly.lma.logger.LogLevel
import com.ooooonly.lma.logger.LogType
import java.util.*

data class LogItemVO(
    val id: Long,
    val type: LogType,
    val level: LogLevel,
    val identity: String,
    val content: String,
    val date: Date
)