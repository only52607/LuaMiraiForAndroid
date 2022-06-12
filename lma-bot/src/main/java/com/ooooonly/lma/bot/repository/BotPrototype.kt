package com.ooooonly.lma.bot.repository

data class BotPrototype(
    val id: Long,
    val password: String,
    val deviceJson: String,
    val protocolName: String,
    val autoLogin: Boolean
)