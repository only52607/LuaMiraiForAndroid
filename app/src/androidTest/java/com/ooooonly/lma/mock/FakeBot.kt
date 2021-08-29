package com.ooooonly.lma.mock

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import kotlin.coroutines.CoroutineContext

class FakeBot: Bot {
    override val asFriend: Friend = TODO()
    override val asStranger: Stranger = TODO()
    override val configuration: BotConfiguration = BotConfiguration()
    override val eventChannel: EventChannel<BotEvent> = TODO()
    override val friends: ContactList<Friend> = TODO()
    override val groups: ContactList<Group> = TODO()
    override val id: Long = 123456789
    override val isOnline: Boolean = true
    override val logger: MiraiLogger = TODO()
    override val nick: String = "OOOOONLY"
    override val otherClients: ContactList<OtherClient>
    override val strangers: ContactList<Stranger>
    override fun close(cause: Throwable?) {
        TODO("Not yet implemented")
    }
    override suspend fun login() {
        TODO("Not yet implemented")
    }
    override val coroutineContext: CoroutineContext
}
