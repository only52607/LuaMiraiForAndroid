package com.ooooonly.lma.logger.utils

import com.ooooonly.lma.logger.LogLevel
import com.ooooonly.lma.logger.LogType

interface LmaLogger {
    suspend fun log(type: LogType, level: LogLevel, identity: String, content: String)

    suspend fun infoFromBotPrimary(
        content: String,
        identity: String
    ) = log(
        type = LogType.Bot,
        level = LogLevel.Info,
        identity = identity,
        content = content
    )

    suspend fun infoFromBotNetwork(
        content: String,
        identity: String
    ) = log(
        type = LogType.Network,
        level = LogLevel.Info,
        identity = identity,
        content = content
    )

    suspend fun infoFromMiraiConsole(
        content: String,
        identity: String = "Mirai Console"
    ) = log(
        type = LogType.MiraiConsole,
        level = LogLevel.Info,
        identity = identity,
        content = content
    )

    suspend fun infoFromScript(
        scriptName: String,
        content: String,
        identity: String = scriptName
    ) = log(
        type = LogType.Script,
        level = LogLevel.Info,
        identity = identity,
        content = content
    )

    suspend fun errorFromBotPrimary(
        content: String,
        identity: String
    ) = log(
        type = LogType.Bot,
        level = LogLevel.Error,
        identity = identity,
        content = content
    )

    suspend fun errorFromBotNetwork(
        content: String,
        identity: String
    ) = log(
        type = LogType.Network,
        level = LogLevel.Error,
        identity = identity,
        content = content
    )

    suspend fun errorFromMiraiConsole(
        content: String,
        identity: String = "Mirai Console"
    ) = log(
        type = LogType.MiraiConsole,
        level = LogLevel.Error,
        identity = identity,
        content = content
    )

    suspend fun errorFromScript(
        scriptName: String,
        content: String,
        identity: String = scriptName
    ) = log(
        type = LogType.Script,
        level = LogLevel.Error,
        identity = identity,
        content = content
    )
}