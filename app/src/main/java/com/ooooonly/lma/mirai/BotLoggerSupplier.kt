package com.ooooonly.lma.mirai

import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.MiraiLogger

interface BotLoggerSupplier {
    fun supply(bot: Bot): MiraiLogger
}