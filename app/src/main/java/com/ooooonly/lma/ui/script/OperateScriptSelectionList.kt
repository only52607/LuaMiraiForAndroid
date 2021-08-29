package com.ooooonly.lma.ui.script

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.selection.SelectionItem
import com.ooooonly.lma.ui.components.selection.SelectionList
import kotlinx.coroutines.launch

@Composable
fun OperateScriptSelectionList(
    onDeleteScriptSelected: () -> Unit,
) {
    val deleteScriptItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.script_delete,
            contentDescriptionResId = R.string.script_delete,
            iconImageVector = Icons.Filled.Delete,
            onClick = onDeleteScriptSelected
        )
    }
    SelectionList(items = listOf(deleteScriptItem))
}