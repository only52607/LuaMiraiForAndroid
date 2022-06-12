package com.ooooonly.lma.bot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ooooonly.lma.bot.BotStatus
import com.ooooonly.lma.bot.repository.BotInstanceManger
import com.ooooonly.lma.bot.repository.BotPrototype
import com.ooooonly.lma.bot.repository.BotPrototypeRepository
import com.ooooonly.lma.datastore.entity.BotEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.network.LoginFailedException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BotViewModel @Inject constructor(
    private val botPrototypeRepository: BotPrototypeRepository,
    private val botInstanceManger: BotInstanceManger
): ViewModel() {

    private val botItemVOs = LinkedHashMap<Long, BotItemVO>()

    private val _botItemVOList = MutableStateFlow(botItemVOs.values.toList())
    val botItemVOList: StateFlow<List<BotItemVO>> = _botItemVOList

    private var _initialized = MutableStateFlow(false)
    val initialized: StateFlow<Boolean> = _initialized

    init {
        viewModelScope.launch {
            listenForBotChanged()
            initializeBotList()
            _initialized.emit(true)
        }
    }

    private fun buildBotItemVO(botId: Long): BotItemVO {
        val bot = botInstanceManger.getBot(botId)
        return when {
            bot != null && bot.isActive && bot.isOnline -> BotItemVO(
                id = botId,
                nickName = bot.nick,
                avatarUrl = bot.avatarUrl,
                status = BotStatus.Instantiated.Online
            )
            bot != null && bot.isActive && !bot.isOnline -> BotItemVO(
                id = botId,
                nickName = bot.nick,
                avatarUrl = bot.avatarUrl,
                status = BotStatus.Instantiated.Offline
            )
            else -> BotItemVO(
                id = botId,
                nickName = "未登录",
                avatarUrl = "",
                status = BotStatus.UnInstantiated
            )
        }
    }

    private fun emitBotItemVOList() {
        viewModelScope.launch {
            _botItemVOList.emit(botItemVOs.values.toList())
        }
    }

    private fun listenForBotChanged() {
        GlobalEventChannel.parentScope(viewModelScope).filter {
            it is BotOnlineEvent || it is BotOfflineEvent || it is BotAvatarChangedEvent || it is BotNickChangedEvent
        }.subscribeAlways<BotEvent> {
            val originItemVO = botItemVOs[it.bot.id] ?: buildBotItemVO(it.bot.id)
            when(this) {
                is BotOnlineEvent -> botItemVOs[it.bot.id] = originItemVO.copy(status = BotStatus.Instantiated.Online)
                is BotNickChangedEvent -> botItemVOs[it.bot.id] = originItemVO.copy(nickName = bot.nick)
                is BotAvatarChangedEvent -> botItemVOs[it.bot.id] = originItemVO.copy(avatarUrl = bot.avatarUrl)
                is BotOfflineEvent -> {
                    when(this) {
                        is BotOfflineEvent.Active -> botItemVOs[it.bot.id] = originItemVO.copy(status = BotStatus.UnInstantiated)
                        is BotOfflineEvent.Force -> botItemVOs[it.bot.id] = originItemVO.copy(status = BotStatus.UnInstantiated)
                        else ->  botItemVOs[it.bot.id] = originItemVO.copy(status = BotStatus.Instantiated.Offline)
                    }
                }
            }
            emitBotItemVOList()
        }
    }

    private suspend fun initializeBotList() {
        botPrototypeRepository.getAllBotPrototypes().forEach { prototype ->
            botItemVOs[prototype.id] = buildBotItemVO(prototype.id)
            if (prototype.autoLogin) loginBot(prototype.id)
        }
        emitBotItemVOList()
    }

    fun saveBotPrototype(botPrototype: BotPrototype) {
        viewModelScope.launch {
            botPrototypeRepository.saveBot(botPrototype)
            botItemVOs[botPrototype.id] = buildBotItemVO(botPrototype.id)
            if (botPrototype.autoLogin) {
                loginBot(botPrototype.id)
            }
        }
        emitBotItemVOList()
    }

    fun loginBot(botId: Long) {
        viewModelScope.launch {
            val botPrototype = botPrototypeRepository.getBotPrototype(botId) ?: throw Exception("bot $botId not found")
            val botInstance = botInstanceManger.getBot(botId)?.takeIf { it.isActive } ?: botInstanceManger.createBotByPrototype(botPrototype)
            val originBotItemVO = botItemVOs[botId] ?: buildBotItemVO(botId)
            botInstance.launch {
                try {
                    botItemVOs[botId] = originBotItemVO.copy(status = BotStatus.Instantiated.Logging)
                    emitBotItemVOList()
                    botInstance.login()
                } catch (e: LoginFailedException) {
                    botItemVOs[botId] = originBotItemVO.copy(status = BotStatus.LoginFailed(e))
                    emitBotItemVOList()
                    val botLogger = botInstance.configuration.botLoggerSupplier(botInstance)
                    botLogger.error(e.message, e)
                }
            }
        }
    }

    fun closeBot(botId: Long) {
        val originBotItemVO = botItemVOs[botId] ?: buildBotItemVO(botId)
        botItemVOs[botId] = originBotItemVO.copy(status = BotStatus.UnInstantiated)
        emitBotItemVOList()
        viewModelScope.launch {
            botInstanceManger.getBot(botId)?.closeAndJoin()
        }
    }

    fun reLoginBot(botId: Long) {
        closeBot(botId)
        loginBot(botId)
    }

    fun removeBot(botId: Long) {
        closeBot(botId)
        viewModelScope.launch {
            botPrototypeRepository.removeBotPrototypeById(botId)
        }
        botItemVOs.remove(botId)
        emitBotItemVOList()
    }
}