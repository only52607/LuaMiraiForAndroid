package com.ooooonly.lma.script

import com.github.only52607.luamirai.core.script.BotScript
import com.ooooonly.lma.datastore.entity.ScriptItem

interface ScriptBuilder {
    suspend fun buildBotScript(item: ScriptItem): BotScript
}