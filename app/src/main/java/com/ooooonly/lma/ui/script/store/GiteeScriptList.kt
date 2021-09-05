package com.ooooonly.lma.ui.script.store

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.EmptyView
import com.ooooonly.lma.utils.GiteeFile
import kotlinx.coroutines.launch

@Composable
fun GiteeScriptList(
    file: GiteeFile,
    onFileClick: (GiteeFile) -> Unit,
    onImportFile: (GiteeFile) -> Unit
) {
    val refreshState = rememberSwipeRefreshState(true)
    var list by remember {
        mutableStateOf(listOf<GiteeFile>())
    }
    val coroutineScope = rememberCoroutineScope()
    suspend fun loadChildFiles(useCache: Boolean = true) {
        refreshState.isRefreshing = true
        list = file.listFiles(useCache)
        refreshState.isRefreshing = false
    }
    LaunchedEffect(file) {
        loadChildFiles()
    }
    SwipeRefresh(
        state = refreshState,
        onRefresh = { coroutineScope.launch { loadChildFiles(useCache = false) } },
    ) {
        if (list.isNotEmpty()) {
            LazyColumn {
                items(list) { file ->
                    GiteeScriptItem(file = file, onClick = { onFileClick(file) }, onImport = { onImportFile(file) })
                }
            }
        } else {
            if (!refreshState.isRefreshing) {
                EmptyView(pictureResId = R.drawable.bg_blank_3, messageResId = R.string.script_empty)
            }
        }
    }
}