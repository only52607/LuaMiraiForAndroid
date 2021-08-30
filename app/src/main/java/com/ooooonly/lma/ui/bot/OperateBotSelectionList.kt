package com.ooooonly.lma.ui.bot

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ooooonly.lma.R
import com.ooooonly.lma.mirai.BotPhase
import com.ooooonly.lma.ui.components.selection.SelectionItem
import com.ooooonly.lma.ui.components.selection.SelectionList

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun OperateBotSelectionList(
    botPhase: BotPhase,
    onEditBotSelected: () -> Unit,
    onRemoveBotSelected: () -> Unit,
    onReLoginBotSelected: () -> Unit,
    onLogoutBotSelected: () -> Unit,
    onLoginBotSelected: () -> Unit,
) {
    val editBotItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.bot_edit,
            contentDescriptionResId = R.string.bot_edit,
            iconImageVector = Icons.Filled.Edit,
            onClick = onEditBotSelected
        )
    }
    val removeBotItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.bot_delete,
            contentDescriptionResId = R.string.bot_delete,
            iconImageVector = Icons.Filled.Delete,
            onClick = onRemoveBotSelected
        )
    }
    val reLoginBotItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.bot_re_login,
            contentDescriptionResId = R.string.bot_re_login,
            iconImageVector = Icons.Filled.Refresh,
            onClick = onReLoginBotSelected
        )
    }
    val logoutBotItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.bot_logout,
            contentDescriptionResId = R.string.bot_logout,
            iconImageVector = Icons.Filled.Logout,
            onClick = onLogoutBotSelected
        )
    }
    val loginBotItem = remember {
        SelectionItem.ImageVectorIcon(
            labelResId = R.string.bot_login,
            contentDescriptionResId = R.string.bot_login,
            iconImageVector = Icons.Filled.Login,
            onClick = onLoginBotSelected
        )
    }

    val botSelectionItems: List<SelectionItem> = buildList<SelectionItem> {
        if (botPhase is BotPhase.UnInstantiated) {
            add(loginBotItem)
        }
        add(editBotItem)
        if (botPhase is BotPhase.Instantiated.Online) {
            add(logoutBotItem)
        }
        if (botPhase is BotPhase.Instantiated.Online || botPhase is BotPhase.Instantiated.Offline || botPhase is BotPhase.LoginFailed) {
            add(reLoginBotItem)
        }
        add(removeBotItem)
    }

    SelectionList(items = botSelectionItems)
}