package com.ooooonly.lma.ui.log

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.R
import com.ooooonly.lma.utils.ripperClickable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LogConfiguration(
    state: LogState = rememberLogState()
) {
    Column {
        ListItem(
            icon = {
                Checkbox(
                    checked = state.autoScroll,
                    onCheckedChange = state::autoScroll::set
                )
            },
            modifier = Modifier.ripperClickable { state.autoScroll = !state.autoScroll }
        ) {
            Text(stringResource(R.string.log_filter_auto_scroll))
        }
        ListItem(
            icon = {
                Checkbox(
                    checked = state.displayState.showDate,
                    onCheckedChange = state.displayState::showDate::set
                )
            },
            modifier = Modifier.ripperClickable {
                state.displayState.showDate = !state.displayState.showDate
            }
        ) {
            Text(stringResource(R.string.log_filter_show_date))
        }
        ListItem(
            icon = {
                Checkbox(
                    checked = state.displayState.showIdentity,
                    onCheckedChange = state.displayState::showIdentity::set
                )
            },
            modifier = Modifier.ripperClickable {
                state.displayState.showIdentity = !state.displayState.showIdentity
            }
        ) {
            Text(stringResource(R.string.log_filter_show_identity))
        }
        ListItem(
            icon = {
                Checkbox(
                    checked = state.filterState.showBotMessageLog,
                    onCheckedChange = state.filterState::showBotMessageLog::set
                )
            },
            modifier = Modifier.ripperClickable {
                state.filterState.showBotMessageLog = !state.filterState.showBotMessageLog
            }
        ) {
            Text(stringResource(R.string.log_filter_show_bot_message_log))
        }
        ListItem(
            icon = {
                Checkbox(
                    checked = state.filterState.showBotNetLog,
                    onCheckedChange = state.filterState::showBotNetLog::set
                )
            },
            modifier = Modifier.ripperClickable {
                state.filterState.showBotNetLog = !state.filterState.showBotNetLog
            }
        ) {
            Text(stringResource(R.string.log_filter_show_bot_net_log))
        }
        ListItem(
            icon = {
                Checkbox(
                    checked = state.filterState.showScriptOutput,
                    onCheckedChange = state.filterState::showScriptOutput::set
                )
            },
            modifier = Modifier.ripperClickable {
                state.filterState.showScriptOutput = !state.filterState.showScriptOutput
            }
        ) {
            Text(stringResource(R.string.log_filter_script_output))
        }
    }
}

class LogState {
    var autoScroll by mutableStateOf(true)
    val filterState = LogFilterState()
    val displayState = LogDisplayState()
}

class LogFilterState {
    var showBotMessageLog by mutableStateOf(true)
    var showBotNetLog by mutableStateOf(true)
    var showScriptOutput by mutableStateOf(true)
}

class LogDisplayState {
    var showDate by mutableStateOf(false)
    var showIdentity by mutableStateOf(true)
}

private val LogStateSaver = run {
    mapSaver<LogState>(
        save = {
            mapOf(
                "autoScroll" to it.autoScroll,
                "showDate" to it.displayState.showDate,
                "showIdentity" to it.displayState.showIdentity,
                "showBotMessageLog" to it.filterState.showBotMessageLog,
                "showBotNetLog" to it.filterState.showBotNetLog,
                "showScriptOutput" to it.filterState.showScriptOutput,
            )
        },
        restore = {
            LogState().apply {
                autoScroll = it["autoScroll"] as Boolean
                displayState.showDate = it["showDate"] as Boolean
                displayState.showIdentity = it["showIdentity"] as Boolean
                filterState.showBotMessageLog = it["showBotMessageLog"] as Boolean
                filterState.showBotNetLog = it["showBotNetLog"] as Boolean
                filterState.showScriptOutput = it["showScriptOutput"] as Boolean
            }
        }
    )
}

@Composable
fun rememberLogState() = rememberSaveable(saver = LogStateSaver) {
    LogState()
}