package com.ooooonly.lma.ui.log

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.R
import com.ooooonly.lma.log.LogState
import com.ooooonly.lma.model.entity.LogEntity
import com.ooooonly.lma.log.LogViewModel
import com.ooooonly.lma.ui.components.EmptyView
import kotlinx.coroutines.launch

@Composable
fun LogScreen(
    logViewModel: LogViewModel,
    navigationIcon: @Composable () -> Unit
) {
    val logState = logViewModel.logState
    val listState = rememberLazyListState()
    //    val isBottom by remember {
    //        derivedStateOf {
    //            listState.layoutInfo.totalItemsCount == listState.layoutInfo.visibleItemsInfo.size + listState.firstVisibleItemIndex
    //        }
    //    }
    LaunchedEffect(logViewModel.logs) {
        if (logState.autoScroll) {
            kotlin.runCatching {
                listState.scrollToItem(listState.layoutInfo.totalItemsCount - listState.layoutInfo.visibleItemsInfo.size + 1)
            }
        }
    }
    LogScreen(
        logs = logViewModel.logs,
        navigationIcon = navigationIcon,
        state = listState,
        loading = !logViewModel.initialized,
        logState = logState,
        onClearLog = {
            logViewModel.clearLog()
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LogScreen(
    logs: List<LogEntity>,
    navigationIcon: @Composable () -> Unit,
    state: LazyListState,
    loading: Boolean = false,
    logState: LogState,
    onClearLog: () -> Unit
) {
    val scaffoldState: BackdropScaffoldState =
        rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.log_title)) },
                backgroundColor = MaterialTheme.colors.surface,
                navigationIcon = navigationIcon,
                actions = {
                    IconButton(onClick = onClearLog) {
                        Icon(Icons.Filled.Delete, contentDescription = null)
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                if (scaffoldState.isRevealed) scaffoldState.conceal()
                                else scaffoldState.reveal()
                            }
                        }
                    ) {
                        Icon(Icons.Filled.FilterList, contentDescription = null)
                    }
                }
            )
        },
        backLayerBackgroundColor = MaterialTheme.colors.surface,
        frontLayerContent = {
            if(logs.isEmpty()) {
                EmptyView(pictureResId = R.drawable.bg_blank_4, messageResId = R.string.log_empty)
            } else {
                LogList(
                    logs = logs,
                    onClickLogEntity = {},
                    modifier = Modifier.fillMaxSize(),
                    listState = state,
                    displayState = logState.displayState
                )
            }
        },
        backLayerContent = {
            LogConfiguration(logState)
        }
    )
}