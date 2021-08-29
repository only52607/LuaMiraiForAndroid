package com.ooooonly.lma.ui.script

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.selection.SelectionItem
import com.ooooonly.lma.ui.components.selection.SelectionList

@Composable
fun AddScriptSelectionList(
    onAddScriptFromFileSelected: () -> Unit,
    onAddScriptFromURLSelected: () -> Unit,
    onAddScriptFromStoreSelected: () -> Unit,
) {
    val addScriptFromFileItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.script_add_from_file,
            contentDescriptionResId = R.string.script_add_from_file,
            iconImageVector = Icons.Filled.Storage,
            onClick = onAddScriptFromFileSelected
        )
    }

    val addScriptFromURLItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.script_add_from_url,
            contentDescriptionResId = R.string.script_add_from_url,
            iconImageVector = Icons.Filled.CloudDownload,
            onClick = onAddScriptFromURLSelected
        )
    }

    val addScriptFromStoreItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.script_add_from_store,
            contentDescriptionResId = R.string.script_add_from_store,
            iconImageVector = Icons.Filled.Store,
            onClick = onAddScriptFromStoreSelected
        )
    }

    SelectionList(items = listOf(addScriptFromFileItem, addScriptFromURLItem, addScriptFromStoreItem))
}