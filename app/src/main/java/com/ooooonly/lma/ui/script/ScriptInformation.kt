package com.ooooonly.lma.ui.script

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.R
import com.ooooonly.lma.script.BotScriptInstanceOwner
import com.ooooonly.lma.script.ScriptPhase
import com.ooooonly.lma.script.ScriptState
import com.ooooonly.lma.ui.components.text.Subtitle2
import com.ooooonly.lma.utils.readableFileSize
import com.ooooonly.luaMirai.base.BotScriptFileSource
import com.ooooonly.luaMirai.base.BotScriptSource
import com.ooooonly.luaMirai.base.BotScriptURLSource
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
            Subtitle2("${stringResource(R.string.script_label_name)}:")
            Text(instance.header.getOrDefault("name", ""))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Subtitle2("${stringResource(R.string.script_label_version)}:")
            Text(instance.header.getOrDefault("version", ""))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Subtitle2("${stringResource(R.string.script_label_author)}:")
            Text(instance.header.getOrDefault("author", ""))
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Subtitle2("${stringResource(R.string.script_label_size)}:")
            Text(instance.source?.size?.readableFileSize ?: "")
        }
        if (instance.source is BotScriptURLSource) {
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth()) {
                Subtitle2("${stringResource(R.string.script_label_source)}:")
                Text(instance.source?.brief ?: "")
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Subtitle2("${stringResource(R.string.script_label_description)}:")
            Text(instance.header.getOrDefault("description", ""))
        }
    }
}

val BotScriptSource.brief: String
    get() = when(this) {
            is BotScriptURLSource -> this.url.toString()
            is BotScriptFileSource -> this.file.path
            else -> "Unknown"
        }