package com.ooooonly.lma.ui.script

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.R
import com.ooooonly.lma.script.ScriptState
import net.mamoe.mirai.utils.MiraiInternalApi

@OptIn(MiraiInternalApi::class)
@Composable
fun ScriptInformation(
    scriptState: ScriptState
) {
    Column(Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth()) {
            Text("${stringResource(R.string.script_label_name)}:", style = MaterialTheme.typography.subtitle2)
            Text(scriptState.instance.header.getOrDefault("name", ""))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Text("${stringResource(R.string.script_label_version)}:", style = MaterialTheme.typography.subtitle2)
            Text(scriptState.instance.header.getOrDefault("version", ""))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Text("${stringResource(R.string.script_label_author)}:", style = MaterialTheme.typography.subtitle2)
            Text(scriptState.instance.header.getOrDefault("author", ""))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Text("${stringResource(R.string.script_label_description)}:", style = MaterialTheme.typography.subtitle2)
            Text(scriptState.instance.header.getOrDefault("description", ""))
        }
    }
}