package com.ooooonly.lma.ui.script.store

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.R
import com.ooooonly.lma.script.ScriptViewModel
import com.ooooonly.lma.utils.GiteeFile
import net.mamoe.mirai.utils.MiraiInternalApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(MiraiInternalApi::class, ExperimentalMaterialApi::class)
@Composable
fun ScriptStoreScreen(
    scriptCenterRoot: GiteeFile,
    scriptViewModel: ScriptViewModel,
    onBack: () -> Unit
) {
    var currentGiteeScriptFile by remember {
        mutableStateOf(scriptCenterRoot)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.script_store_title)) },
                backgroundColor = MaterialTheme.colors.surface,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) {
        GiteeScriptList (
            file = currentGiteeScriptFile,
            onFileClick = {
                if (it.isDirectory) {
                    currentGiteeScriptFile = it
                }
            }
        )
    }
}