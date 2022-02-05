package com.ooooonly.lma.mirai.impl

import android.content.SharedPreferences
import com.ooooonly.lma.mirai.BotConstructor
import com.ooooonly.lma.mirai.LoginSolverDelegate
import com.ooooonly.lma.datastore.entity.BotItem
import com.ooooonly.lma.di.BotWorkingDirectory
import com.ooooonly.lma.logger.LmaLogger
import com.ooooonly.lma.utils.getBooleanSafe
import com.ooooonly.lma.utils.getIntSafe
import com.ooooonly.lma.utils.getLongSafe
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.SimpleLogger
import java.io.File
import javax.inject.Inject

class BotConstructorImpl @Inject constructor(
    @BotWorkingDirectory private val botWorkingDirBase: File,
    private val loginSolverDelegate: LoginSolverDelegate,
    private val lmaLogger: com.ooooonly.lma.logger.LmaLogger,
    private val sharedPreferences: SharedPreferences
): BotConstructor {
    override fun createBot(item: BotItem): Bot {
        return BotFactory.newBot(
            item.id,
            item.password,
            BotConfiguration.Default.apply {
                workingDir = File(botWorkingDirBase, item.id.toString())
                deviceInfo = { _ -> item.deviceInfo }
                protocol = item.protocol
                loginSolver = loginSolverDelegate
                botLoggerSupplier = { bot ->
                    SimpleLogger { priority: SimpleLogger.LogPriority, message: String?, e: Throwable? ->
                        if (priority == SimpleLogger.LogPriority.ERROR) {
                            lmaLogger.errorFromBotPrimary(bot, message ?: "")
                        } else {
                            lmaLogger.infoFromBotPrimary(bot, message ?: "")
                        }
                    }
                }
                networkLoggerSupplier = { bot ->
                    SimpleLogger { priority: SimpleLogger.LogPriority, message: String?, e: Throwable? ->
                        if (priority == SimpleLogger.LogPriority.ERROR) {
                            lmaLogger.infoFromBotPrimary(bot, message ?: "")
                        } else {
                            lmaLogger.infoFromBotPrimary(bot, message ?: "")
                        }
                    }
                }
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
}