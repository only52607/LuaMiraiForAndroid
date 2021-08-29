package com.ooooonly.lma.ui.script

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CloudDownload
import androidx.compose.material.icons.twotone.InsertDriveFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.R
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.ripperClickable(onClick = onClick).fillMaxWidth().padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (scriptState.instance.source is BotScriptURLSource) {
                Icon(Icons.TwoTone.CloudDownload, contentDescription = "Remote Script")
            } else {
                Icon(Icons.TwoTone.InsertDriveFile, contentDescription = "Local Script")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = scriptState.instance.header["name"] ?: stringResource(R.string.script_unknown_name),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = "${scriptState.instance.header["version"] ?: stringResource(R.string.script_label_version)} by ${scriptState.instance.header["author"] ?: stringResource(R.string.script_unknown_author)}",
                    style = MaterialTheme.typography.body2
                )
            }
        }
        if (scriptState.loading) {
            Spacer(modifier = Modifier.width(16.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.width(16.dp))
        }
        Switch(checked = scriptState.enabled, onCheckedChange = onEnableChanged)
    }
}