package com.ooooonly.lma.ui.components.dialog

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
    ) {
    Dialog (
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
            contentColor = contentColorFor(MaterialTheme.colors.surface),
        ) {
            content()
        }
    }
}