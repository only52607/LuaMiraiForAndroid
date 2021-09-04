package com.ooooonly.lma.ui.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun SimpleAlertDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    confirmText: String? = null,
    dismissText: String? = null,
    titleText: String? = null,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    BaseDialog(
        properties = properties,
        onDismissRequest = onDismiss,
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                if (titleText != null) {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                }
                content()
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (dismissText != null) {
                        TextButton(onClick = onDismiss) {
                            Text(dismissText)
                        }
                    }
                    if (confirmText != null) {
                        TextButton(onClick = onConfirm) {
                            Text(confirmText)
                        }
                    }
                }
            }
        }
    )
}