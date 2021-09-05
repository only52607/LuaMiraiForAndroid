package com.ooooonly.lma.ui.components.selection

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.utils.ripperClickable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectionList(
    items: List<SelectionItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items) {
            ListItem(
                icon = { SelectionIcon(it) },
                text = { Text(stringResource(it.labelResId), color = it.color ?: Color.Unspecified) },
                modifier = Modifier.ripperClickable { it.onClick() }
            )
        }
    }
}