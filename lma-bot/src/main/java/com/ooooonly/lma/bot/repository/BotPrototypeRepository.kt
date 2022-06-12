package com.ooooonly.lma.bot.repository

interface BotPrototypeRepository {
    suspend fun saveBot(bot: BotPrototype)

    suspend fun getBotPrototype(botId: Long): BotPrototype?

    suspend fun getAllBotPrototypes(): List<BotPrototype>

    suspend fun removeBotPrototypeById(botId: Long)
}