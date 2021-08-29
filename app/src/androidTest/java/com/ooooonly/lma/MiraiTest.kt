package com.ooooonly.lma

import net.mamoe.mirai.BotFactory
import org.junit.Test

class AndroidMiraiTest {
    @Test
    fun createBot() {
        BotFactory.newBot(2912775665, "123456")
        println("create success")
    }
}