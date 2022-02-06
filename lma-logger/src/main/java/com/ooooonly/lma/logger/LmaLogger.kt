package com.ooooonly.lma.logger

import com.github.only52607.luamirai.core.script.BotScript
import com.ooooonly.lma.datastore.entity.LogItem
import net.mamoe.mirai.Bot

interface LmaLogger {
    suspend fun log(from: Int, level: Int, identity: String, content: String)

    suspend fun infoFromBotPrimary(
        bot: Bot,
        content: String,
        identity: String = "${bot.id}"
    ) = log(
        from = LogItem.FROM_BOT_PRIMARY,
        level = LogItem.LEVEL_INFO,
        identity = identity,
        content = content
    )

    suspend fun infoFromBotNetwork(
        bot: Bot,
        content: String,
        identity: String = "${bot.id}"
    ) = log(
        from = LogItem.FROM_BOT_NETWORK,
        level = LogItem.LEVEL_INFO,
        identity = identity,
        content = content
    )

    suspend fun infoFromMiraiConsole(
        content: String,
        identity: String = "Mirai Console"
    ) = log(
        from = LogItem.FROM_MCL,
        level = LogItem.LEVEL_INFO,
        identity = identity,
        content = content
    )

    suspend fun infoFromScript(
        script: BotScript,
        content: String,
        identity: String = script.header?.get("name") ?: "Unknown script"
    ) = log(
        from = LogItem.FROM_SCRIPT,
        level = LogItem.LEVEL_INFO,
        identity = identity,
        content = content
    )

    suspend fun errorFromBotPrimary(
        bot: Bot,
        content: String,
        identity: String = "${bot.id}"
    ) = log(
        from = LogItem.FROM_BOT_PRIMARY,
        level = LogItem.LEVEL_ERROR,
        identity = identity,
        content = content
    )

    suspend fun errorFromBotNetwork(
        bot: Bot,
        content: String,
        identity: String = "${bot.id}"
    ) = log(
        from = LogItem.FROM_BOT_NETWORK,
        level = LogItem.LEVEL_ERROR,
        identity = identity,
        content = content
    )

    suspend fun errorFromMiraiConsole(
        content: String,
        identity: String = "Mirai Console"
    ) = log(
        from = LogItem.FROM_MCL,
        level = LogItem.LEVEL_ERROR,
        identity = identity,
        content = content
    )

    suspend fun errorFromScript(
        script: BotScript,
        content: String,
        identity: String = script.header?.get("name") ?: "Unknown script"
    ) = log(
        from = LogItem.FROM_SCRIPT,
        level = LogItem.LEVEL_ERROR,
        identity = identity,
        content = content
    )
}