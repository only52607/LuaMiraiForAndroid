package com.ooooonly.lma.ui.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    content: String = "",
    onContentChange: (String) -> Unit,
    confirmText: String = "保存",
    dismissText: String = "取消",
    titleText: String = "",
    additionalContent: @Composable () -> Unit = {},
    showTextField: Boolean = true,
    textFieldMaxLine: Int = 1
) {
    BaseDialog(
        onDismissRequest = onDismiss,
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                additionalContent()
                if (showTextField) {
                    TextField(
                        maxLines = textFieldMaxLine,
                        value = content,
                        onValueChange = onContentChange,
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text(dismissText)
                    }
                    TextButton(
                        onClick = onConfirm
                    ) {
                        Text(confirmText)
                    }
                }
            }
        }
    )
}

//@Composable
//fun TextFieldDialog(
//    onDismiss: () -> Unit = {},
//    onConfirm: () -> Unit = {},
//    content: String = "",
//    onContentChange: (String) -> Unit,
//    confirmText: String = "保存",
//    dismissText: String = "取消",
//    titleText: String = "",
//    additionalContent: @Composable () -> Unit = {}
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = {
//            Text(
//                text = titleText,
//                style = MaterialTheme.typography.subtitle1,
//                fontWeight = FontWeight.Bold
//            )
//        },
//        text = {
//            Column(modifier = Modifier.fillMaxWidth()) {
//                additionalContent()
//                TextField(
//                    value = content,
//                    onValueChange = onContentChange,
//                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
//                )
//            }
//        },
//        confirmButton = {
//            TextButton(
//                onClick = onConfirm
//            ) {
//                Text(confirmText)
//            }
//        },
//        dismissButton = {
//            TextButton(
//                onClick = onDismiss
//            ) {
//                Text(dismissText)
//            }
//        }
//    )
//}