package com.ooooonly.lma.ui.log

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ooooonly.lma.log.LogDisplayState
import com.ooooonly.lma.datastore.entity.LogItem

@Composable
fun LogList(
    logs: List<LogItem>,
    onClickLogEntity: (LogItem) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState,
    displayState: LogDisplayState
) {
    LazyColumn(modifier = modifier, state = listState) {
        items(logs) { log ->
            LogItem(logItem = log, onClick = { onClickLogEntity(log) }, displayState = displayState)
        }
    }
}