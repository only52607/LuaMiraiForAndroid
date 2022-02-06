package com.ooooonly.lma.ui.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.datastore.entity.LogItem
import com.ooooonly.lma.utils.ripperClickable
import java.text.SimpleDateFormat
import java.util.*

private val dataFormatter by lazy {
    SimpleDateFormat.getInstance()
}

private fun Date.display(visible: Boolean, before: String = "", after: String = ""): String {
    if (visible) {
        return "$before${dataFormatter.format(this)}$after"
    }
    return ""
}

@Composable
fun LogItem(
    logItem: LogItem,
    onClick: () -> Unit,
    displayState: LogDisplayState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.ripperClickable(onClick = onClick).fillMaxWidth().padding(8.dp)
    ) {
        Text(
            "${
                logItem.date.display(displayState.showDate, after = " ")
            }${
                if (displayState.showIdentity) "${logItem.identity}->" else ""
            }${logItem.content}",
            color = when(logItem.level) {
                LogItem.LEVEL_ERROR -> MaterialTheme.colors.error
                else -> Color.Unspecified
            }
        )
    }
}