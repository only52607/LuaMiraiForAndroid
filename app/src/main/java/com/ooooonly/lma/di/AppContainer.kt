package com.ooooonly.lma.di

import com.ooooonly.lma.mirai.LoginSolverDelegate
import com.ooooonly.lma.mirai.MiraiViewModel
import com.ooooonly.lma.script.ScriptViewModel
import com.ooooonly.lma.utils.GiteeFile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppContainer @Inject constructor(
    val scriptViewModel: ScriptViewModel,
    val miraiViewModel: MiraiViewModel,
    val logViewModel: LogViewModel,
    val loginSolverDelegate: LoginSolverDelegate,
    @ScriptCenterRoot val scriptCenterRoot: GiteeFile
)