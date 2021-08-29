package com.ooooonly.lma.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun <T> DropDownList(
    items: List<T>,
    itemRender: @Composable (T) -> Unit,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box {
        TextButton(onClick = { expanded = !expanded }, modifier = modifier){
            itemRender(selectedItem)
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onItemSelected(item)
                }) {
                    itemRender(item)
                }
            }
        }
    }

}
