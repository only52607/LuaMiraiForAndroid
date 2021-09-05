package com.ooooonly.lma.script

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ooooonly.lma.model.entity.ScriptEntity
import com.ooooonly.luaMirai.base.BotScript
import kotlinx.coroutines.Job

class ScriptState(val entity: ScriptEntity) {
    var phase by mutableStateOf(ScriptPhase.Pending as ScriptPhase)
}

sealed class ScriptPhase(val description: String) {
    object Pending : ScriptPhase("准备中")

    class Creating(override val job: Job) : ScriptPhase("正在创建"), JobOwner

    class FailedOnCreating(override val cause: Throwable) : ScriptPhase("创建失败"), ThrowableOwner

    class Disabled(override val instance: BotScript) : ScriptPhase("未启用"), BotScriptInstanceOwner

    class Enabled(override val instance: BotScript) : ScriptPhase("已启用"), BotScriptInstanceOwner

    class Loading(override val instance: BotScript, override val job: Job) : ScriptPhase("启用中"),
        JobOwner, BotScriptInstanceOwner

    class FailedOnLoading(override val instance: BotScript, override val cause: Throwable) :
        ScriptPhase("启用失败"), ThrowableOwner, BotScriptInstanceOwner

    class Updating(override val instance: BotScript, override val job: Job) : ScriptPhase("更新中"),
        JobOwner, BotScriptInstanceOwner

    class FailedOnUpdating(override val instance: BotScript, override val cause: Throwable) :
        ScriptPhase("更新失败"), ThrowableOwner, BotScriptInstanceOwner
}

interface BotScriptInstanceOwner {
    val instance: BotScript
}

interface JobOwner {
    val job: Job
}

interface ThrowableOwner {
    val cause: Throwable
}