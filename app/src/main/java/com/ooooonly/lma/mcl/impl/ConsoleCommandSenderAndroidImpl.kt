package com.ooooonly.lma.mcl.impl

import com.ooooonly.lma.mcl.MclLoggerProvider
import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.message.data.Message
import javax.inject.Inject

@OptIn(ConsoleFrontEndImplementation::class)
class ConsoleCommandSenderAndroidImpl @Inject constructor(
    mclLoggerProvider: MclLoggerProvider
) : MiraiConsoleImplementation.ConsoleCommandSenderImpl {

    private val logger = mclLoggerProvider.provideMclLogger("send")

    override suspend fun sendMessage(message: String) {
        logger.info(message)
    }

    override suspend fun sendMessage(message: Message) {
        logger.info(message.contentToString())
    }
}