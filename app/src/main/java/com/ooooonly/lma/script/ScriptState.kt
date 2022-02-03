package com.ooooonly.lma.script

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.only52607.luamirai.core.script.BotScript
import com.ooooonly.lma.datastore.entity.ScriptEntity
import com.ooooonly.lma.datastore.entity.toBotScriptSource
import kotlinx.coroutines.Job

class ScriptState(val entity: ScriptEntity) {
    val scriptSource = entity.toBotScriptSource()
    var phase by mutableStateOf(ScriptPhase.InActive as ScriptPhase)
}

sealed class ScriptPhase(val description: String) {
    object InActive : ScriptPhase("未运行")

    class Active(override val script: BotScript): ScriptPhase("正在运行"), BotScriptInstanceOwner

    class Creating(override val job: Job) : ScriptPhase("正在创建"), JobOwner

    class FailedOnCreating(override val cause: Throwable) : ScriptPhase("创建失败"), ThrowableOwner
}

interface BotScriptInstanceOwner {
    val script: BotScript
}

interface JobOwner {
    val job: Job
}

interface ThrowableOwner {
    val cause: Throwable
}