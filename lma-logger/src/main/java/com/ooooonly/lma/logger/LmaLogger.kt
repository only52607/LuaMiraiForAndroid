package com.ooooonly.lma.logger

import com.github.only52607.luamirai.core.script.BotScript
import com.ooooonly.lma.datastore.entity.LogItem
import net.mamoe.mirai.Bot

interface LmaLogger {
    suspend fun log(from: Int, level: Int, identity: String, content: String)

    suspend fun infoFromBotPrimary(
        bot: Bot,
        content: String
    ) = log(
        from = LogItem.FROM_BOT_PRIMARY,
        level = LogItem.LEVEL_INFO,
        identity = "${bot.id}",
        content = content
    )

    suspend fun infoFromBotNetwork(
        bot: Bot,
        content: String
    ) = log(
        from = LogItem.FROM_BOT_NETWORK,
        level = LogItem.LEVEL_INFO,
        identity = "${bot.id}",
        content = content
    )

    suspend fun infoFromMiraiConsole(
        content: String
    ) = log(
        from = LogItem.FROM_MCL,
        level = LogItem.LEVEL_INFO,
        identity = "Mirai Console",
        content = content
    )

    suspend fun infoFromScript(
        script: BotScript,
        content: String
    ) = log(
        from = LogItem.FROM_SCRIPT,
        level = LogItem.LEVEL_INFO,
        identity = script.header?.get("name") ?: "Unknown script",
        content = content
    )

    suspend fun errorFromBotPrimary(
        bot: Bot,
        content: String
    ) = log(
        from = LogItem.FROM_BOT_PRIMARY,
        level = LogItem.LEVEL_ERROR,
        identity = "${bot.id}",
        content = content
    )

    suspend fun errorFromBotNetwork(
        bot: Bot,
        content: String
    ) = log(
        from = LogItem.FROM_BOT_NETWORK,
        level = LogItem.LEVEL_ERROR,
        identity = "${bot.id}",
        content = content
    )

    suspend fun errorFromMiraiConsole(
        content: String
    ) = log(
        from = LogItem.FROM_MCL,
        level = LogItem.LEVEL_ERROR,
        identity = "Mirai Console",
        content = content
    )

    suspend fun errorFromScript(
        script: BotScript,
        content: String
    ) = log(
        from = LogItem.FROM_SCRIPT,
        level = LogItem.LEVEL_ERROR,
        identity = script.header?.get("name") ?: "Unknown script",
        content = content
    )
}