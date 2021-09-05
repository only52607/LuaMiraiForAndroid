package com.ooooonly.lma.ui.script

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.selection.SelectionItem
import com.ooooonly.lma.ui.components.selection.SelectionList
import kotlinx.coroutines.launch

@Composable
fun OperateScriptSelectionList(
    onDeleteScriptSelected: () -> Unit,
    onUpdateScriptSelected: () -> Unit,
) {
    val deleteScriptItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.script_delete,
            contentDescriptionResId = R.string.script_delete,
            iconImageVector = Icons.Filled.Delete,
            onClick = onDeleteScriptSelected
        )
    }
    val updateScriptItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.script_update,
            contentDescriptionResId = R.string.script_update,
            iconImageVector = Icons.Filled.Update,
            onClick = onUpdateScriptSelected
        )
    }
    SelectionList(items = listOf(updateScriptItem, deleteScriptItem))
}