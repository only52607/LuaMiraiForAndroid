package com.ooooonly.lma.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.mamoe.mirai.utils.BotConfiguration

@Composable
fun MiraiProtocolDropDown(
    selectedProtocol: BotConfiguration.MiraiProtocol,
    onChangeProtocol: (BotConfiguration.MiraiProtocol) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropDownList(
        items = listOf(
            BotConfiguration.MiraiProtocol.ANDROID_PHONE,
            BotConfiguration.MiraiProtocol.ANDROID_PAD,
            BotConfiguration.MiraiProtocol.ANDROID_WATCH
        ),
        itemRender = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MiraiProtocolIcon(protocol = it)
                Spacer(Modifier.width(5.dp))
                Text(it.name)
            }
        },
        selectedItem = selectedProtocol,
        onItemSelected = onChangeProtocol,
        modifier = modifier
    )
}