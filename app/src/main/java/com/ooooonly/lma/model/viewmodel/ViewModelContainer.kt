package com.ooooonly.lma.model.viewmodel

import com.ooooonly.lma.log.LogViewModel
import com.ooooonly.lma.mirai.MiraiViewModel
import com.ooooonly.lma.script.ScriptViewModel

data class ViewModelContainer(
    val scriptViewModel: ScriptViewModel,
    val miraiViewModel: MiraiViewModel,
    val logViewModel: LogViewModel
)