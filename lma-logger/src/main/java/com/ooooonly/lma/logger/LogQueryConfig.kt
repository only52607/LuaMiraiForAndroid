package com.ooooonly.lma.logger

data class LogQueryConfig(
    val containBotMessageLog: Boolean,
    val containBotNetLog: Boolean,
    val containScriptOutput: Boolean,
    val containMclLog: Boolean,
    val limits: Long = 200
)