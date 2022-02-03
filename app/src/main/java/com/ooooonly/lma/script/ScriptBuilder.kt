package com.ooooonly.lma.script

import com.github.only52607.luamirai.core.script.BotScript
import com.ooooonly.lma.datastore.entity.ScriptEntity

interface ScriptBuilder {
    suspend fun buildBotScript(entity: ScriptEntity): BotScript
}