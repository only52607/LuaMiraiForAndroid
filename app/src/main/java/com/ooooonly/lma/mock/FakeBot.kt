package com.ooooonly.lma.mock

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import kotlin.coroutines.CoroutineContext

class FakeBot: Bot {
    override val asFriend: Friend
        get() = throw NotImplementedError()
    override val asStranger: Stranger
        get() = throw NotImplementedError()
    override val configuration: BotConfiguration = BotConfiguration()
    override val eventChannel: EventChannel<BotEvent>
        get() = throw NotImplementedError()
    override val friends: ContactList<Friend>
        get() = throw NotImplementedError()
    override val groups: ContactList<Group>
        get() = throw NotImplementedError()
    override val id: Long = 123456789
    override val isOnline: Boolean = true
    override val logger: MiraiLogger
        get() = throw NotImplementedError()
    override val nick: String = "OOOOONLY"
    override val otherClients: ContactList<OtherClient>
        get() = throw NotImplementedError()
    override val strangers: ContactList<Stranger>
        get() = throw NotImplementedError()
    override fun close(cause: Throwable?) {
        throw Exception()
    }
    override suspend fun login() {
        throw Exception()
    }
    override val coroutineContext: CoroutineContext
        get() = throw NotImplementedError()
}