package com.ooooonly.lma

import android.util.Log
import net.mamoe.mirai.BotFactory
import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.superclasses

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class MiraiTest {
    @Test
    fun createBot() {
       println(Int::class.allSuperclasses)
        println("create success")
    }
}