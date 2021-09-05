package com.ooooonly.lma.ui.script

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.R
import com.ooooonly.lma.script.BotScriptInstanceOwner
import com.ooooonly.lma.script.ScriptPhase
import com.ooooonly.lma.script.ScriptState
import net.mamoe.mirai.utils.MiraiInternalApi

@OptIn(MiraiInternalApi::class)
@Composable
fun ScriptInformation(
    scriptState: ScriptState
) {
    val phase = scriptState.phase
    if (phase !is BotScriptInstanceOwner) return
    if (phase is ScriptPhase.Updating || phase is ScriptPhase.FailedOnUpdating) return
    val instance = phase.instance
    Column(Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth()) {
            Text("${stringResource(R.string.script_label_name)}:", style = MaterialTheme.typography.subtitle2)
            Text(instance.header.getOrDefault("name", ""))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Text("${stringResource(R.string.script_label_version)}:", style = MaterialTheme.typography.subtitle2)
            Text(instance.header.getOrDefault("version", ""))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Text("${stringResource(R.string.script_label_author)}:", style = MaterialTheme.typography.subtitle2)
            Text(instance.header.getOrDefault("author", ""))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Text("${stringResource(R.string.script_label_description)}:", style = MaterialTheme.typography.subtitle2)
            Text(instance.header.getOrDefault("description", ""))
        }
    }
}