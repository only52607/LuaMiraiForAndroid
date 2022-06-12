package com.ooooonly.lma.bot

sealed class BotStatus(val description: String) {
    sealed class Instantiated(description: String) : BotStatus(description) {
        object Online : Instantiated("在线")
        object Logging : Instantiated("登录中")
        object Offline : Instantiated("重连中")
    }
    class LoginFailed(val cause: Throwable) : BotStatus("登录失败")
    object UnInstantiated : BotStatus("离线")
}