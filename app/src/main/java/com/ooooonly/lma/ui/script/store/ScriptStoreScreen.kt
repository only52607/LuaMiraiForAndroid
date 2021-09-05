package com.ooooonly.lma.ui.script.store

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.R
import com.ooooonly.lma.script.ScriptViewModel
import com.ooooonly.lma.utils.GiteeFile
import kotlinx.coroutines.launch
import net.mamoe.mirai.utils.MiraiInternalApi

@OptIn(MiraiInternalApi::class, ExperimentalMaterialApi::class)
@Composable
fun ScriptStoreScreen(
    scriptCenterRoot: GiteeFile,
    scriptViewModel: ScriptViewModel,
    scaffoldState: ScaffoldState,
    onBack: () -> Unit
) {
    var currentGiteeScriptFile by remember {
        mutableStateOf(scriptCenterRoot)
    }
    val coroutineScope = rememberCoroutineScope()
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
        val importSuccessfulText = stringResource(R.string.script_import_successful)
        GiteeScriptList (
            file = currentGiteeScriptFile,
            onFileClick = {
                if (it.isDirectory) {
                    currentGiteeScriptFile = it
                }
            },
            onImportFile = {
                scriptViewModel.addScript(it.rawUrl)
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(importSuccessfulText)
                }
            }
        )
    }
}