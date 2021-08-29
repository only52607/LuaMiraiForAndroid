package com.ooooonly.lma.script

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ooooonly.lma.model.entity.ScriptEntity
import com.ooooonly.luaMirai.base.BotScript
import kotlinx.coroutines.Job
import net.mamoe.mirai.utils.MiraiInternalApi

class ScriptState
@OptIn(MiraiInternalApi::class)
constructor(val instance: BotScript, val entity: ScriptEntity) {
    var loading by mutableStateOf(false)
    var enabled by mutableStateOf(false)
    var job: Job? = null
}