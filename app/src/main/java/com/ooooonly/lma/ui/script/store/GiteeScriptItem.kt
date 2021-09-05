package com.ooooonly.lma.ui.script.store

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.twotone.CloudDownload
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.R
import com.ooooonly.lma.utils.GiteeFile
import com.ooooonly.lma.utils.ripperClickable

@Composable
fun GiteeScriptItem(
    file: GiteeFile,
    onClick: () -> Unit,
    onImport: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.ripperClickable(onClick = onClick).fillMaxWidth().padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (file.isDirectory) {
                Icon(Icons.TwoTone.Folder, contentDescription = "Folder")
            } else {
                Icon(Icons.TwoTone.CloudDownload, contentDescription = "Remote Script")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = file.fileName,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
        Box {
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More")
            }
            DropdownMenu(expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    onImport()
                }) {
                    Text(stringResource(R.string.script_import))
                }
            }
        }
    }
}