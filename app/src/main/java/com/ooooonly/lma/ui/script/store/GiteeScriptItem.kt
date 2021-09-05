package com.ooooonly.lma.ui.script.store

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CloudDownload
import androidx.compose.material.icons.twotone.FileDownload
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.utils.GiteeFile
import com.ooooonly.lma.utils.ripperClickable

@Composable
fun GiteeScriptItem(
    file: GiteeFile,
    onClick: () -> Unit
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
                Text(
                    text = file.rawUrl.toString(),
                    style = MaterialTheme.typography.body2
                )
            }
        }
        IconButton(onClick = onClick) {
            Icon(Icons.TwoTone.FileDownload, contentDescription = "Folder")
        }
    }
}