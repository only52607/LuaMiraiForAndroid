package com.ooooonly.lma.ui.script

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.selection.SelectionItem
import com.ooooonly.lma.ui.components.selection.SelectionList

@Composable
fun OperateScriptSelectionList(
    onDeleteScriptSelected: () -> Unit,
    onUpdateScriptSelected: () -> Unit,
) {
    val deleteScriptItem = SelectionItem.ImageVectorIcon(
        color = MaterialTheme.colors.error,
        labelResId = R.string.script_delete,
        contentDescriptionResId = R.string.script_delete,
        iconImageVector = Icons.Filled.Delete,
        onClick = onDeleteScriptSelected
    )
    val updateScriptItem = SelectionItem.ImageVectorIcon(
        labelResId = R.string.script_update,
        contentDescriptionResId = R.string.script_update,
        iconImageVector = Icons.Filled.Update,
        onClick = onUpdateScriptSelected
    )
    SelectionList(items = listOf(updateScriptItem, deleteScriptItem))
}