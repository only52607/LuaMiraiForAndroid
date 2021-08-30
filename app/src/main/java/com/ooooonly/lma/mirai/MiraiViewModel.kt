package com.ooooonly.lma.mirai

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ooooonly.lma.data.dao.BotDao
import com.ooooonly.lma.model.entity.BotEntity
import kotlinx.coroutines.*
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.network.LoginFailedException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class MiraiViewModel @Inject constructor(
    private val botDao: BotDao,
    private val botConstructor: BotConstructor
): CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob()

    private val _botStates = mutableStateListOf<BotState>()
    val botStates: List<BotState> = _botStates

    private var _initialized by mutableStateOf(false)
    val initialized: Boolean = _initialized

    init {
        launch {
            subscribeBotEvent()
            loadBotStates()
            _initialized = true
        }
    }

    private fun findBotStateById(botId: Long): BotState? {
        return botStates.find { it.entity.id == botId }
    }

    private fun subscribeBotEvent() {
        GlobalEventChannel.parentScope(this).filter {
            it is BotOnlineEvent || it is BotOfflineEvent || it is BotAvatarChangedEvent || it is BotNickChangedEvent
        }.subscribeAlways<BotEvent> {
            when(this) {
                is BotOnlineEvent -> findBotStateById(bot.id)?.phase = BotPhase.Instantiated.Online(this.bot)
                is BotNickChangedEvent -> findBotStateById(bot.id)?.phase = BotPhase.Instantiated.Online(this.bot)
                is BotAvatarChangedEvent -> findBotStateById(bot.id)?.phase = BotPhase.Instantiated.Online(this.bot)
                is BotOfflineEvent -> {
                    when(this) {
                        is BotOfflineEvent.Active -> findBotStateById(bot.id)?.phase = BotPhase.UnInstantiated
                        is BotOfflineEvent.Force -> findBotStateById(bot.id)?.phase = BotPhase.UnInstantiated
                        else ->  findBotStateById(bot.id)?.phase = BotPhase.Instantiated.Offline(this.bot)
                    }
                }
            }
        }
    }

    private suspend fun loadBotStates() {
        val botEntities = botDao.loadAllBots()
        botEntities.forEach { entity ->
            _botStates.add(BotState(entity).also {
                if (entity.autoLogin) loginBotState(it)
            })
        }
    }

    fun saveBotByParameter(botEntity: BotEntity, originId: Long? = null) {
        if (originId != null) {
            val originState = findBotStateById(originId)
            if (originState != null) {
                launch {
                    botDao.deleteBots(originState.entity)
                }
                originState.entity = botEntity
            }
        } else {
            _botStates.add(BotState(botEntity).also {
                if (botEntity.autoLogin) loginBotState(it)
            })
        }
        launch {
            botDao.saveBots(botEntity)
        }
    }

    fun loginBotState(botState: BotState) {
        val botInstance = botConstructor.createBot(botState.entity)
        botState.phase = BotPhase.Instantiated.Logging(botInstance)
        launch {
            try {
                botInstance.login()
            } catch (e: LoginFailedException) {
                val botLogger = botInstance.configuration.botLoggerSupplier(botInstance)
                botState.phase = BotPhase.LoginFailed(e)
                botLogger.error(e.message, e)
            }
        }
    }

    fun reLoginBotState(botState: BotState) {
        closeBotState(botState)
        loginBotState(botState)
    }

    fun closeBotState(botState: BotState) {
        launch {
            val phase = botState.phase
            if (phase is BotPhase.Instantiated) {
                phase.instance.closeAndJoin()
                botState.phase = BotPhase.UnInstantiated
            }
        }
    }

    fun removeBotState(botState: BotState) {
        closeBotState(botState)
        launch {
            botDao.deleteBots(botState.entity)
        }
        _botStates.remove(botState)
    }
}