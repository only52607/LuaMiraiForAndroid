package com.ooooonly.lma.ui.script

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ooooonly.lma.R
import com.ooooonly.lma.script.ScriptState
import com.ooooonly.lma.ui.components.EmptyView
import net.mamoe.mirai.utils.MiraiInternalApi

@OptIn(MiraiInternalApi::class)
@Composable
fun ScriptList(
    scripts: List<ScriptState>,
    onClickScript: (ScriptState) -> Unit,
    onScriptEnableChanged: (ScriptState, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if(scripts.isEmpty()) {
        EmptyView(pictureResId = R.drawable.bg_blank_3, messageResId = R.string.script_empty)
    } else {
        LazyColumn {
            items(scripts) { script ->
                ScriptItem(scriptState = script, onClick = { onClickScript(script) }, onEnableChanged = { onScriptEnableChanged(script, it) })
            }
        }
    }
}