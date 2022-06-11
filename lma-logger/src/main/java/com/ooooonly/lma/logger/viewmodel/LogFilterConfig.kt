package com.ooooonly.lma.logger.viewmodel

data class LogFilterConfig(
    val containBotMessageLog: Boolean = true,
    val containBotNetLog: Boolean = true,
    val containMclLog: Boolean = true,
    val containScriptOutput: Boolean = true
)