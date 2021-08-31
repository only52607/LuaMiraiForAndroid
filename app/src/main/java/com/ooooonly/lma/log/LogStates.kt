package com.ooooonly.lma.log

import androidx.compose.runtime.*

class LogState {
    var autoScroll by mutableStateOf(true)
    val filterState = LogFilterState()
    val displayState = LogDisplayState()
}

class LogFilterState {
    var showBotMessageLog by mutableStateOf(true)
    var showBotNetLog by mutableStateOf(true)
    var showScriptOutput by mutableStateOf(true)
    var showMclLog by mutableStateOf(true)
}

class LogDisplayState {
    var showDate by mutableStateOf(false)
    var showIdentity by mutableStateOf(true)
}

//private val LogStateSaver = run {
//    mapSaver(
//        save = {
//            mapOf(
//                "autoScroll" to it.autoScroll,
//                "showDate" to it.displayState.showDate,
//                "showIdentity" to it.displayState.showIdentity,
//                "showBotMessageLog" to it.filterState.showBotMessageLog,
//                "showBotNetLog" to it.filterState.showBotNetLog,
//                "showScriptOutput" to it.filterState.showScriptOutput,
//            )
//        },
//        restore = {
//            LogState().apply {
//                autoScroll = it["autoScroll"] as Boolean
//                displayState.showDate = it["showDate"] as Boolean
//                displayState.showIdentity = it["showIdentity"] as Boolean
//                filterState.showBotMessageLog = it["showBotMessageLog"] as Boolean
//                filterState.showBotNetLog = it["showBotNetLog"] as Boolean
//                filterState.showScriptOutput = it["showScriptOutput"] as Boolean
//            }
//        }
//    )
//}

@Composable
fun rememberLogState() = remember {
    LogState()
}