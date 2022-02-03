package com.ooooonly.lma.log

import com.github.only52607.luamirai.core.script.BotScript
import com.ooooonly.lma.datastore.entity.LogEntity
import net.mamoe.mirai.Bot

interface LmaLogger {
    fun clear()

    fun log(from: Int, level: Int, identity: String, content: String)

    fun infoFromBotPrimary(
        bot: Bot,
        content: String
    ) = log(
        from = LogEntity.FROM_BOT_PRIMARY,
        level = LogEntity.LEVEL_INFO,
        identity = "${bot.id}",
        content = content
    )

    fun infoFromBotNetwork(
        bot: Bot,
        content: String
    ) = log(
        from = LogEntity.FROM_BOT_NETWORK,
        level = LogEntity.LEVEL_INFO,
        identity = "${bot.id}",
        content = content
    )

    fun infoFromMiraiConsole(
        content: String
    ) = log(
        from = LogEntity.FROM_MCL,
        level = LogEntity.LEVEL_INFO,
        identity = "Mirai Console",
        content = content
    )

    fun infoFromScript(
        script: BotScript,
        content: String
    ) = log(
        from = LogEntity.FROM_SCRIPT,
        level = LogEntity.LEVEL_INFO,
        identity = script.header?.get("name") ?: "Unknown script",
        content = content
    )

    fun errorFromBotPrimary(
        bot: Bot,
        content: String
    ) = log(
        from = LogEntity.FROM_BOT_PRIMARY,
        level = LogEntity.LEVEL_ERROR,
        identity = "${bot.id}",
        content = content
    )

    fun errorFromBotNetwork(
        bot: Bot,
        content: String
    ) = log(
        from = LogEntity.FROM_BOT_NETWORK,
        level = LogEntity.LEVEL_ERROR,
        identity = "${bot.id}",
        content = content
    )

    fun errorFromMiraiConsole(
        content: String
    ) = log(
        from = LogEntity.FROM_MCL,
        level = LogEntity.LEVEL_ERROR,
        identity = "Mirai Console",
        content = content
    )

    fun errorFromScript(
        script: BotScript,
        content: String
    ) = log(
        from = LogEntity.FROM_SCRIPT,
        level = LogEntity.LEVEL_ERROR,
        identity = script.header?.get("name") ?: "Unknown script",
        content = content
    )
}