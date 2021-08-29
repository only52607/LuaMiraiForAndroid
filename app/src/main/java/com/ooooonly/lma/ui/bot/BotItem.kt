package com.ooooonly.lma.ui.bot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ooooonly.lma.mirai.BotState
import com.ooooonly.lma.mirai.instanceOrNull
import com.ooooonly.lma.ui.components.BotPhaseBadge
import com.ooooonly.lma.ui.components.MiraiProtocolIcon
import com.ooooonly.lma.utils.getAvatarUrlById
import com.ooooonly.lma.utils.nickOrNull
import com.ooooonly.lma.utils.ripperClickable

@Composable
fun BotItem(
    botState: BotState,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.ripperClickable(onClick = onClick).fillMaxWidth().padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberImagePainter(data = getAvatarUrlById(botState.entity.id)),
                modifier = Modifier.size(32.dp).clip(CircleShape),
                contentDescription = "Avatar image of ${botState.entity.id}"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = botState.phase.instanceOrNull?.nickOrNull ?: "",
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = botState.entity.id.toString(),
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            BotPhaseBadge(botState.phase)
            Spacer(modifier = Modifier.width(16.dp))
            MiraiProtocolIcon(botState.entity.protocol)
        }
    }
}