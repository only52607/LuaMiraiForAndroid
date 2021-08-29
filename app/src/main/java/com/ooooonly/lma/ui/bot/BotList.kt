package com.ooooonly.lma.ui.bot

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ooooonly.lma.R
import com.ooooonly.lma.mirai.BotState
import com.ooooonly.lma.ui.components.EmptyView

@Composable
fun BotList(
    bots: List<BotState>,
    onClickBot: (BotState) -> Unit
) {
    if(bots.isEmpty()) {
        EmptyView(pictureResId = R.drawable.bg_blank_1, messageResId = R.string.bot_empty)
    } else {
        LazyColumn {
            items(bots) { bot ->
                BotItem(botState = bot, onClick = { onClickBot(bot) })
            }
        }
    }
}

@Preview
@Composable
fun PreviewBotList() {
    BotList(listOf(), {})
}