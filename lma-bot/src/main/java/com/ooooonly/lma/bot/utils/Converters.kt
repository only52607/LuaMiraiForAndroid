package com.ooooonly.lma.bot.utils

import net.mamoe.mirai.utils.BotConfiguration

fun String.parseMiraiProtocol(): BotConfiguration.MiraiProtocol {
    return when (this) {
        "ANDROID_PHONE" -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
        "ANDROID_PAD" -> BotConfiguration.MiraiProtocol.ANDROID_PAD
        "ANDROID_WATCH" -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
        "IPAD" -> BotConfiguration.MiraiProtocol.IPAD
        "MACOS" -> BotConfiguration.MiraiProtocol.MACOS
        else -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
    }
}

fun BotConfiguration.MiraiProtocol.toMiraiProtocolString(): String {
    return when (this) {
        BotConfiguration.MiraiProtocol.ANDROID_PHONE -> "ANDROID_PHONE"
        BotConfiguration.MiraiProtocol.ANDROID_PAD -> "ANDROID_PAD"
        BotConfiguration.MiraiProtocol.ANDROID_WATCH -> "ANDROID_WATCH"
        BotConfiguration.MiraiProtocol.IPAD -> "IPAD"
        BotConfiguration.MiraiProtocol.MACOS -> "MACOS"
    }
}