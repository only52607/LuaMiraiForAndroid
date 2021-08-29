package com.ooooonly.lma.mirai

import com.ooooonly.lma.model.entity.BotEntity
import net.mamoe.mirai.Bot

interface BotConstructor {
    fun createBot(entity: BotEntity): Bot
}