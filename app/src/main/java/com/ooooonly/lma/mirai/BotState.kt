package com.ooooonly.lma.mirai

import androidx.compose.runtime.*
import com.ooooonly.lma.model.entity.BotEntity
import net.mamoe.mirai.Bot
import net.mamoe.mirai.network.LoginFailedException

class BotState(
    entity: BotEntity
) {
    var phase: BotPhase by mutableStateOf(BotPhase.UnInstantiated)
    var entity: BotEntity by mutableStateOf(entity)
}

sealed class BotPhase(val description: String) {
    sealed class Instantiated(val instance: Bot, description: String) : BotPhase(description) {
        class Online(instance: Bot) : Instantiated(instance, "在线")
        class Logging(instance: Bot) : Instantiated(instance, "登录中")
        class Offline(instance: Bot) : Instantiated(instance, "重连中")
    }
    class LoginFailed(val cause: LoginFailedException) : BotPhase("登录失败")
    class UnInstantiated : BotPhase("离线")
    companion object {
        val UnInstantiated = UnInstantiated()
    }
}

val BotPhase.instanceOrNull: Bot?
    get() = if (this is BotPhase.Instantiated) this.instance else null