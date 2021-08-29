package com.ooooonly.lma.model.parcel.mirai

import android.os.Parcelable
import com.ooooonly.lma.model.parcel.ParcelableAdapter
import kotlinx.parcelize.Parcelize
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration

@Parcelize
class ParcelableBot(
    val id: Long = 0,
    val nick: String = "",
    val isOnline: Boolean = false,
    val avatarUrl: String = "",
    val password: String = "",
    val configuration: ParcelableBotConfiguration =  ParcelableBotConfiguration(BotConfiguration.Default)
): Parcelable, ParcelableAdapter<Bot> {
    constructor(bot: Bot) : this(bot.id, bot.nick, bot.isOnline, bot.avatarUrl, "", ParcelableBotConfiguration(bot.configuration))

    override fun read(): Bot {
        return BotFactory.newBot(id, password, configuration.read())
    }
}