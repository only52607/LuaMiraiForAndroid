package com.ooooonly.lma.logger.repository

import com.ooooonly.lma.logger.LogLevel
import com.ooooonly.lma.logger.LogType
import java.util.*

class Log(
    val id: Long? = null,
    val type: LogType,
    val level: LogLevel,
    val identity: String,
    val content: String,
    val date: Date
)