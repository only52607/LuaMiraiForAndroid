package com.ooooonly.lma.ui.script

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.R
import com.ooooonly.lma.script.ScriptState
import com.ooooonly.lma.script.ScriptViewModel
import com.ooooonly.lma.ui.components.dialog.ProgressDialogHost
import com.ooooonly.lma.ui.components.dialog.TextFieldDialog
import com.ooooonly.lma.ui.components.dialog.rememberProgressDialogState
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mamoe.mirai.utils.MiraiInternalApi
import java.net.URL

@OptIn(MiraiInternalApi::class, ExperimentalMaterialApi::class)
@Composable
fun ScriptScreen(
    scriptViewModel: ScriptViewModel,
    toScriptMarket: () -> Unit,
    navigationIcon: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var currentScript by remember { mutableStateOf(null as ScriptState?) }
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val startActivityAtomic = remember { atomic(0) }
    var showURLImportDialog by remember { mutableStateOf(false) }
    val progressDialogState = rememberProgressDialogState()

    val importMessage = stringResource(R.string.script_importing)
    val importCancelText = stringResource(R.string.script_cancel_import)
    val importScriptFromURL: (String) -> Unit = remember {
        { url ->
            progressDialogState.postProgress(
                message = importMessage,
                cancelText = importCancelText,
                cancellable = true
            ) {
                withContext(Dispatchers.IO) {
                    scriptViewModel.addScript(URL(url).openStream())
                }
            }
        }
    }

    val contentResolver = LocalContext.current.contentResolver
    val importScriptFromURI: (Uri) -> Unit = remember {
        { uri ->
            progressDialogState.postProgress(
                message = importMessage,
                cancelText = importCancelText,
                cancellable = true
            ) {
                contentResolver.openInputStream(uri)?.let { scriptViewModel.addScript(it) }
            }
        }
    }

    val activityResultRegistry = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    val addScriptFromFile: () -> Unit = remember {
        {
            activityResultRegistry?.register(
                "ADD_SCRIPT_${startActivityAtomic.getAndIncrement()}",
                ActivityResultContracts.OpenDocument()
            ) { uri ->
                if (uri == null) return@register
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                importScriptFromURI(uri)
            }?.launch(arrayOf("*/*"))
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { drawerState.isClosed }.filter { it }.collect { currentScript = null }
    }

    BottomDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            val script = currentScript
            if (script != null) {
                Column {
                    ScriptInformation(script)
                    OperateScriptSelectionList(
                        onDeleteScriptSelected = {
                            coroutineScope.launch { drawerState.close() }
                            currentScript?.let(scriptViewModel::deleteScript)
                        }
                    )
                }
            } else {
                AddScriptSelectionList(
                    onAddScriptFromFileSelected = {
                        coroutineScope.launch { drawerState.close() }
                        addScriptFromFile()
                    },
                    onAddScriptFromURLSelected = {
                        coroutineScope.launch { drawerState.close() }
                        showURLImportDialog = true
                    },
                    onAddScriptFromStoreSelected = {
                        coroutineScope.launch { drawerState.close() }
                        toScriptMarket()
                    },
                )
            }
        },
        content = {
            ScriptScreen(
                navigationIcon = navigationIcon,
                onClickFloatingActionButton = { coroutineScope.launch { drawerState.open() } },
                toScriptMarket = toScriptMarket,
                scripts = scriptViewModel.scripts,
                onScriptEnableChanged = { script, enable ->
                    if (enable) scriptViewModel.enableScript(script)
                    else scriptViewModel.disableScript(script)
                },
                onClickScript = {
                    currentScript = it
                    coroutineScope.launch { drawerState.open() }
                },
                loading = !scriptViewModel.initialized
            )
        },
    )

    ProgressDialogHost(progressDialogState = progressDialogState)

    if (showURLImportDialog) {
        var content by remember { mutableStateOf("") }
        TextFieldDialog(
            onDismiss = { showURLImportDialog = false },
            content = content,
            onContentChange = { content = it },
            titleText = stringResource(R.string.script_add_from_url),
            confirmText = stringResource(R.string.script_import),
            onConfirm = {
                showURLImportDialog = false
                importScriptFromURL(content)
            }
        )
    }
}

@OptIn(MiraiInternalApi::class)
@Composable
fun ScriptScreen(
    toScriptMarket: () -> Unit,
    onClickFloatingActionButton: () -> Unit,
    navigationIcon: @Composable () -> Unit,
    scripts: List<ScriptState>,
    onClickScript: (ScriptState) -> Unit,
    onScriptEnableChanged: (ScriptState, Boolean) -> Unit,
    loading: Boolean = false
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.script_title)) },
                backgroundColor = MaterialTheme.colors.surface,
                navigationIcon = navigationIcon,
                actions = {
                    IconButton(onClick = { toScriptMarket() }) {
                        Icon(Icons.Filled.Store, contentDescription = "Store")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickFloatingActionButton,
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        },
    ) {
        ScriptList(scripts, onClickScript, onScriptEnableChanged)
    }
}