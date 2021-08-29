package com.ooooonly.lma.ui.bot

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.selection.SelectionItem
import com.ooooonly.lma.ui.components.selection.SelectionList

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun AddBotSelectionList(
    onAddBotSelected: () -> Unit,
) {
    val addBotItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.bot_add,
            contentDescriptionResId = R.string.bot_add,
            iconImageVector = Icons.Filled.Add,
            onClick = onAddBotSelected
        )
    }
    SelectionList(items = listOf(addBotItem))
}