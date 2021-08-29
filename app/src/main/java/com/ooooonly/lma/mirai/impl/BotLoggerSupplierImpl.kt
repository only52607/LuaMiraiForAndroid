package com.ooooonly.lma.mirai.impl

import com.ooooonly.lma.log.LogViewModel
import com.ooooonly.lma.mirai.BotLoggerSupplier
import com.ooooonly.lma.model.entity.LogEntity
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.SimpleLogger
import javax.inject.Inject

class BotLoggerSupplierImpl @Inject constructor(
    private val logViewModel: LogViewModel
) : BotLoggerSupplier {
    override fun supply(bot: Bot): MiraiLogger {
        return SimpleLogger { priority: SimpleLogger.LogPriority, message: String?, e: Throwable? ->
            if (priority == SimpleLogger.LogPriority.ERROR) {
                logViewModel.insertLog(
                    LogEntity(
                        from = LogEntity.FROM_BOT_PRIMARY,
                        level = LogEntity.LEVEL_ERROR,
                        content = message ?: "",
                        identity = bot.id.toString()
                    )
                )
            } else {
                logViewModel.insertLog(
                    LogEntity(
                        from = LogEntity.FROM_BOT_PRIMARY,
                        level = LogEntity.LEVEL_INFO,
                        content = message ?: "",
                        identity = bot.id.toString()
                    )
                )
            }
        }
    }
}