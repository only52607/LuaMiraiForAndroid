package com.ooooonly.lma.mirai.impl

import android.content.SharedPreferences
import com.ooooonly.lma.mirai.BotConstructor
import com.ooooonly.lma.mirai.BotLoggerSupplier
import com.ooooonly.lma.mirai.LoginSolverDelegate
import com.ooooonly.lma.mirai.NetworkLoggerSupplier
import com.ooooonly.lma.model.AppFiles
import com.ooooonly.lma.model.entity.BotEntity
import com.ooooonly.lma.utils.getBooleanSafe
import com.ooooonly.lma.utils.getIntSafe
import com.ooooonly.lma.utils.getLongSafe
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import java.io.File
import javax.inject.Inject

class BotConstructorImpl @Inject constructor(
    private val appFiles: AppFiles,
    private val loginSolverDelegate: LoginSolverDelegate,
    private val botLoggerSupplier: BotLoggerSupplier,
    private val networkLoggerSupplier: NetworkLoggerSupplier,
    private val sharedPreferences: SharedPreferences
): BotConstructor {
    override fun createBot(entity: BotEntity): Bot {
        return BotFactory.newBot(
            entity.id,
            entity.password,
            BotConfiguration.Default.apply {
                workingDir = File(appFiles.botWorkingDirBase, entity.id.toString())
                deviceInfo = { _ -> entity.deviceInfo }
                protocol = entity.protocol
                loginSolver = loginSolverDelegate
                botLoggerSupplier = this@BotConstructorImpl.botLoggerSupplier::supply
                networkLoggerSupplier = this@BotConstructorImpl.networkLoggerSupplier::supply
                heartbeatPeriodMillis = sharedPreferences.getLongSafe("heartbeatPeriodMillis", 60) * 1000
                statHeartbeatPeriodMillis = sharedPreferences.getLongSafe("statHeartbeatPeriodMillis", 300) * 1000
                heartbeatStrategy = when(sharedPreferences.getString("heartbeatStrategy", "STAT_HB")) {
                    "STAT_HB" -> BotConfiguration.HeartbeatStrategy.STAT_HB
                    "REGISTER" -> BotConfiguration.HeartbeatStrategy.REGISTER
                    "NONE" -> BotConfiguration.HeartbeatStrategy.NONE
                    else -> BotConfiguration.HeartbeatStrategy.STAT_HB
                }
                heartbeatTimeoutMillis = sharedPreferences.getLongSafe("heartbeatTimeoutMillis", 5) * 1000
                // firstReconnectDelayMillis = sharedPreferences.getLongSafe("firstReconnectDelayMillis", 5) * 1000
                // reconnectPeriodMillis = sharedPreferences.getLongSafe("reconnectPeriodMillis", 5) * 1000
                reconnectionRetryTimes = sharedPreferences.getIntSafe("reconnectionRetryTimes", Int.MAX_VALUE)
                autoReconnectOnForceOffline = sharedPreferences.getBooleanSafe("autoReconnectOnForceOffline", false)
            })
    }
}