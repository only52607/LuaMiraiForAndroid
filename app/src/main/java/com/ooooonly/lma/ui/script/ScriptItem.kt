package com.ooooonly.lma.ui.script

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CloudDownload
import androidx.compose.material.icons.twotone.Error
import androidx.compose.material.icons.twotone.InsertDriveFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.R
import com.ooooonly.lma.script.BotScriptInstanceOwner
import com.ooooonly.lma.script.ScriptPhase
import com.ooooonly.lma.script.ScriptState
import com.ooooonly.lma.utils.ripperClickable
import com.ooooonly.luaMirai.base.BotScriptURLSource
import net.mamoe.mirai.utils.MiraiInternalApi

@OptIn(MiraiInternalApi::class)
@Composable
fun ScriptItem(
    scriptState: ScriptState,
    onClick: () -> Unit,
    onEnableChanged: (Boolean) -> Unit
) {
    val phase = scriptState.phase
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.ripperClickable(onClick = onClick).fillMaxWidth().padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Show Icon
            when(phase) {
                is ScriptPhase.Updating -> {
                    CircularProgressIndicator()
                }
                is ScriptPhase.Creating -> {
                    CircularProgressIndicator()
                }
                is ScriptPhase.FailedOnCreating -> {
                    Icon(Icons.TwoTone.Error, contentDescription = "Error Script")
                }
                is BotScriptInstanceOwner -> {
                    if (phase.instance.source is BotScriptURLSource) {
                        Icon(Icons.TwoTone.CloudDownload, contentDescription = "Remote Script")
                    } else {
                        Icon(Icons.TwoTone.InsertDriveFile, contentDescription = "Local Script")
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Show Title
            when(phase) {
                is ScriptPhase.Creating -> {
                    Text(
                        text = stringResource(R.string.script_creating),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
                is ScriptPhase.FailedOnCreating -> {
                    Text(
                        text = stringResource(R.string.script_failed_on_creating),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
                is ScriptPhase.Updating -> {
                    Text(
                        text = stringResource(R.string.script_updating),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
                is ScriptPhase.FailedOnUpdating -> {
                    Text(
                        text = stringResource(R.string.script_failed_on_updating),
                        style = MaterialTheme.typography.subtitle2
                    )
                }
                is BotScriptInstanceOwner -> {
                    Column {
                        Text(
                            text = phase.instance.header["name"] ?: stringResource(R.string.script_unknown_name),
                            style = MaterialTheme.typography.subtitle2
                        )
                        Text(
                            text = "${phase.instance.header["version"] ?: stringResource(R.string.script_label_version)} by ${phase.instance.header["author"] ?: stringResource(R.string.script_unknown_author)}",
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }

        // Show extra State
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            when(phase) {
                is ScriptPhase.Loading -> {
                    CircularProgressIndicator()
                }
                is ScriptPhase.FailedOnLoading -> {
                    Icon(Icons.TwoTone.Error, contentDescription = "Error on loading", tint = MaterialTheme.colors.error)
                }
            }
        }

        // Switch
        val switchEnabled = phase is ScriptPhase.Enabled || phase is ScriptPhase.Loading
        Switch(checked = switchEnabled, onCheckedChange = onEnableChanged)
    }
}