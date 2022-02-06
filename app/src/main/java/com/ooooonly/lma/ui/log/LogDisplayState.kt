package com.ooooonly.lma.ui.log

import androidx.compose.runtime.mutableStateOf

class LogDisplayState {
    val autoScrollEnabled = mutableStateOf(true)
    val dateVisible = mutableStateOf(false)
    val identityVisible = mutableStateOf(true)
    val containBotMessageLog = mutableStateOf(true)
    val containBotNetLog = mutableStateOf(true)
    val containMclLog = mutableStateOf(true)
    val containScriptOutput = mutableStateOf(true)
}