package com.ooooonly.lma.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Smartphone
import androidx.compose.material.icons.twotone.Tablet
import androidx.compose.material.icons.twotone.Watch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.mamoe.mirai.utils.BotConfiguration

@Composable
fun MiraiProtocolIcon(
    protocol: BotConfiguration.MiraiProtocol,
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier,
        imageVector = when(protocol) {
            BotConfiguration.MiraiProtocol.ANDROID_PHONE -> Icons.TwoTone.Smartphone
            BotConfiguration.MiraiProtocol.ANDROID_PAD -> Icons.TwoTone.Tablet
            BotConfiguration.MiraiProtocol.ANDROID_WATCH -> Icons.TwoTone.Watch
        }, contentDescription = protocol.name
    )
}