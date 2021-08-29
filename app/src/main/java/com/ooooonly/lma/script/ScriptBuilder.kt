package com.ooooonly.lma.script

import com.ooooonly.lma.model.entity.ScriptEntity
import com.ooooonly.luaMirai.base.BotScript

interface ScriptBuilder {
    suspend fun buildBotScript(entity: ScriptEntity): BotScript?
}