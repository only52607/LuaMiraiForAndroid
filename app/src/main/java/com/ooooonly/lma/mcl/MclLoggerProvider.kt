package com.ooooonly.lma.mcl

import net.mamoe.mirai.utils.MiraiLogger

interface MclLoggerProvider {
    fun provideMclLogger(identity: String?): MiraiLogger
}