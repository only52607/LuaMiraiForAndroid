package com.ooooonly.lma.service.binder

import android.content.Context
import com.ooooonly.lma.aidl.IBotManager
import com.ooooonly.lma.model.parcel.mirai.ParcelableBot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import kotlin.coroutines.CoroutineContext

class BotManagerBinder(val context: Context): IBotManager.Stub(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Job()

    override fun getBotList(): MutableList<ParcelableBot> {
        return Bot.instances.map(::ParcelableBot).toMutableList()
    }

    override fun loginBot(botId: Long) {
        launch { Bot.findInstance(botId)?.login() }
    }

    override fun addBot(bot: ParcelableBot?) {
        if (bot == null) return
        BotFactory.newBot(bot.id, bot.password, bot.configuration.read())
    }
}