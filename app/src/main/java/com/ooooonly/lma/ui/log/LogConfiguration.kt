package com.ooooonly.lma.ui.log

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.R
import com.ooooonly.lma.log.LogState
import com.ooooonly.lma.log.rememberLogState
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
        ListItem(
            icon = {
                Checkbox(
                    checked = state.filterState.showMclLog,
                    onCheckedChange = state.filterState::showMclLog::set
                )
            },
            modifier = Modifier.ripperClickable {
                state.filterState.showMclLog = !state.filterState.showMclLog
            }
        ) {
            Text(stringResource(R.string.log_filter_show_mirai_console))
        }
    }
}