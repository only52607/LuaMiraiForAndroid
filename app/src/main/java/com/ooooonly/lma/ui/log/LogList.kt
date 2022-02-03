package com.ooooonly.lma.ui.log

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ooooonly.lma.log.LogDisplayState
import com.ooooonly.lma.datastore.entity.LogEntity

@Composable
fun LogList(
    logs: List<LogEntity>,
    onClickLogEntity: (LogEntity) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState,
    displayState: LogDisplayState
) {
    LazyColumn(modifier = modifier, state = listState) {
        items(logs) { log ->
            LogItem(logEntity = log, onClick = { onClickLogEntity(log) }, displayState = displayState)
        }
    }
}