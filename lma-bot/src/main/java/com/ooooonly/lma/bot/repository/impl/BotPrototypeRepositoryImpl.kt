package com.ooooonly.lma.bot.repository.impl

import com.ooooonly.lma.bot.repository.BotPrototype
import com.ooooonly.lma.bot.repository.BotPrototypeRepository
import com.ooooonly.lma.datastore.dao.BotDao
import com.ooooonly.lma.datastore.entity.BotEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BotPrototypeRepositoryImpl @Inject constructor(
    private val botDao: BotDao
) : BotPrototypeRepository {

    private val BotPrototype.entityValue get() = BotEntity(
        id = id,
        password = password,
        deviceInfo = deviceJson,
        protocol = protocolName,
        autoLogin = autoLogin
    )

    private val BotEntity.prototypeValue get() = BotPrototype(
        id = id,
        password = password,
        deviceJson = deviceInfo,
        protocolName = protocol,
        autoLogin = autoLogin
    )

    override suspend fun saveBot(bot: BotPrototype) {
        botDao.saveBots(bot.entityValue)
    }

    override suspend fun getBotPrototype(botId: Long): BotPrototype? {
        return botDao.getBotById(botId)?.prototypeValue
    }

    override suspend fun getAllBotPrototypes(): List<BotPrototype> {
        return botDao.selectAllBots().map { it.prototypeValue }
    }

    override suspend fun removeBotPrototypeById(botId: Long) {
        botDao.deleteBotById(botId)
    }
}