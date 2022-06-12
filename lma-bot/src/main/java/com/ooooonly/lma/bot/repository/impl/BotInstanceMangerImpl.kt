package com.ooooonly.lma.bot.repository.impl

import android.content.SharedPreferences
import com.ooooonly.lma.bot.di.BotWorkingDirectory
import com.ooooonly.lma.bot.repository.BotInstanceManger
import com.ooooonly.lma.bot.repository.BotPrototype
import com.ooooonly.lma.bot.utils.LoginSolverDelegate
import com.ooooonly.lma.bot.utils.fromJsonString
import com.ooooonly.lma.bot.utils.parseMiraiProtocol
import com.ooooonly.lma.logger.LogLevel
import com.ooooonly.lma.logger.LogType
import com.ooooonly.lma.logger.utils.LmaLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.DeviceInfo
import net.mamoe.mirai.utils.SimpleLogger
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class BotInstanceMangerImpl @Inject constructor(
    @BotWorkingDirectory private val botWorkingDirBase: File,
    private val loginSolverDelegate: LoginSolverDelegate,
    private val lmaLogger: LmaLogger,
    private val sharedPreferences: SharedPreferences
): BotInstanceManger, CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob()

    override val botInstances: List<Bot>
        get() = Bot.instances

    override fun getBot(botId: Long): Bot? {
        return Bot.getInstanceOrNull(botId)
    }

    private fun SimpleLogger.LogPriority.toLogLevel() = when(this) {
        SimpleLogger.LogPriority.ERROR -> LogLevel.Error
        SimpleLogger.LogPriority.VERBOSE -> LogLevel.Verbose
        SimpleLogger.LogPriority.INFO -> LogLevel.Info
        SimpleLogger.LogPriority.DEBUG -> LogLevel.Debug
        SimpleLogger.LogPriority.WARNING -> LogLevel.Warning
    }

    private fun getBotLogger(bot: Bot) = SimpleLogger { priority: SimpleLogger.LogPriority, message: String?, e: Throwable? ->
             val logType = LogType.Bot
             val logLevel = priority.toLogLevel()
             launch {
                 lmaLogger.log(logType, logLevel, "[${bot.id}]", message ?: "")
             }
        }

    private fun getNetworkLogger(bot: Bot) = SimpleLogger { priority: SimpleLogger.LogPriority, message: String?, e: Throwable? ->
        val logType = LogType.Network
        val logLevel = priority.toLogLevel()
        launch {
            lmaLogger.log(logType, logLevel, "[${bot.id}]", message ?: "")
        }
    }

    override fun createBotByPrototype(botPrototype: BotPrototype): Bot {
        return BotFactory.newBot(
            botPrototype.id,
            botPrototype.password,
            BotConfiguration.Default.apply {
                workingDir = File(botWorkingDirBase, botPrototype.id.toString())
                deviceInfo = { _ -> DeviceInfo.fromJsonString(botPrototype.deviceJson) }
                protocol = botPrototype.protocolName.parseMiraiProtocol()
                loginSolver = loginSolverDelegate
                botLoggerSupplier = ::getBotLogger
                networkLoggerSupplier = ::getNetworkLogger
                heartbeatPeriodMillis = sharedPreferences.getLongSafe("heartbeatPeriodMillis", 60) * 1000
                statHeartbeatPeriodMillis = sharedPreferences.getLongSafe("statHeartbeatPeriodMillis", 300) * 1000
                heartbeatStrategy = when(sharedPreferences.getString("heartbeatStrategy", "STAT_HB")) {
                    "STAT_HB" -> BotConfiguration.HeartbeatStrategy.STAT_HB
                    "REGISTER" -> BotConfiguration.HeartbeatStrategy.REGISTER
                    "NONE" -> BotConfiguration.HeartbeatStrategy.NONE
                    else -> BotConfiguration.HeartbeatStrategy.STAT_HB
                }
                heartbeatTimeoutMillis = sharedPreferences.getLongSafe("heartbeatTimeoutMillis", 5) * 1000
                reconnectionRetryTimes = sharedPreferences.getIntSafe("reconnectionRetryTimes", Int.MAX_VALUE)
                autoReconnectOnForceOffline = sharedPreferences.getBooleanSafe("autoReconnectOnForceOffline", false)
            })
    }

    private fun SharedPreferences.getLongSafe(key: String, defValue: Long):Long {
        return try {
            return getLong(key, defValue)
        } catch (e: Exception) {
            try {
                getString(key, defValue.toString())?.toLong() ?: defValue
            } catch (e:Exception) {
                return defValue
            }
        }
    }

    private fun SharedPreferences.getIntSafe(key: String, defValue: Int):Int {
        return try {
            return getInt(key, defValue)
        } catch (e: Exception) {
            try {
                getString(key, defValue.toString())?.toInt() ?: defValue
            } catch (e:Exception) {
                return defValue
            }
        }
    }

    private fun SharedPreferences.getBooleanSafe(key: String, defValue: Boolean):Boolean {
        return try {
            return getBoolean(key, defValue)
        } catch (e: Exception) {
            try {
                getString(key, defValue.toString())?.toBoolean() ?: defValue
            } catch (e:Exception) {
                return defValue
            }
        }
    }
}