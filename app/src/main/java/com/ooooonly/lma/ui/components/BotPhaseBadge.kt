package com.ooooonly.lma.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.mirai.BotPhase

@Composable
fun BotPhaseBadge(
    botPhase: BotPhase
) {
    if (botPhase is BotPhase.Instantiated.Logging) {
        Box(modifier = Modifier.padding(4.dp)) {
            CircularProgressIndicator()
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
    Text(botPhase.description, color = Color.forBotPhase(botPhase))
}

@Composable
private fun Color.Companion.forBotPhase(botPhase: BotPhase) = when(botPhase) {
    is BotPhase.LoginFailed -> MaterialTheme.colors.error
    is BotPhase.Instantiated.Online -> MaterialTheme.colors.secondary
    else -> Unspecified
}