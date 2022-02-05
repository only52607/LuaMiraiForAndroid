package com.ooooonly.lma.mirai

import com.ooooonly.lma.datastore.entity.BotItem
import net.mamoe.mirai.Bot

interface BotConstructor {
    fun createBot(item: BotItem): Bot
}