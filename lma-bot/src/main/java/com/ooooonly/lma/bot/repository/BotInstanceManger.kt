package com.ooooonly.lma.bot.repository

import net.mamoe.mirai.Bot

interface BotInstanceManger {
    val botInstances: List<Bot>

    fun getBot(botId: Long): Bot?

    fun createBotByPrototype(botPrototype: BotPrototype): Bot
}