package com.ooooonly.lma.bot.viewmodel

import com.ooooonly.lma.bot.BotStatus

data class BotItemVO(
    val id: Long,
    val avatarUrl: String,
    val nickName: String,
    val status: BotStatus
)