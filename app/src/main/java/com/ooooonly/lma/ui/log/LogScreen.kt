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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.EmptyView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LogScreen(
    logViewModel: LogScreenViewModel = viewModel(),
    navigationIcon: @Composable () -> Unit
) {
    val listState = rememberLazyListState()
    val scaffoldState: BackdropScaffoldState =
        rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()
    val logList by logViewModel.logItemList.collectAsState(listOf())
    LaunchedEffect(logViewModel.logItemList) {
        if (logViewModel.logDisplayState.autoScrollEnabled) {
            kotlin.runCatching {
                listState.scrollToItem(listState.layoutInfo.totalItemsCount - listState.layoutInfo.visibleItemsInfo.size + 1)
            }
        }
    }
    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.log_title)) },
                backgroundColor = MaterialTheme.colors.surface,
                navigationIcon = navigationIcon,
                actions = {
                    IconButton(onClick = logViewModel::clearLog) {
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
            if(logList.isEmpty()) {
                EmptyView(pictureResId = R.drawable.bg_blank_4, messageResId = R.string.log_empty)
            } else {
                LogList(
                    logs = logList,
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