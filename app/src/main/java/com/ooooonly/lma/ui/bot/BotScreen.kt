package com.ooooonly.lma.ui.bot

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.mock.FakeBot
import com.ooooonly.lma.mirai.BotState
import com.ooooonly.lma.mirai.MiraiViewModel
import net.mamoe.mirai.Bot
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.drawer.BottomDrawerFix
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun BotScreen(
    miraiViewModel: MiraiViewModel,
    toBotEditScreen: (Long?) -> Unit,
    navigationIcon: @Composable () -> Unit
) {
    BotScreen(
        botStates = miraiViewModel.botStates,
        onCreateBot = { toBotEditScreen(null) },
        onRemoveBotState = miraiViewModel::removeBotState,
        onReLoginBotState = miraiViewModel::reLoginBotState,
        onEditBotState = { toBotEditScreen(it.item.id) },
        onCloseBotState = miraiViewModel::closeBotState,
        onLoginBotState = miraiViewModel::loginBotState,
        navigationIcon = navigationIcon,
        loading = !miraiViewModel.initialized
    )
}

@OptIn(
    ExperimentalMaterialApi::class, androidx.compose.animation.ExperimentalAnimationApi::class,
    ExperimentalStdlibApi::class
)
@Composable
fun BotScreen(
    botStates: List<BotState>,
    onCreateBot: () -> Unit = {},
    onRemoveBotState: (BotState) -> Unit = {},
    onEditBotState: (BotState) -> Unit = {},
    onReLoginBotState: (BotState) -> Unit = {},
    onCloseBotState: (BotState) -> Unit = {},
    onLoginBotState: (BotState) -> Unit = {},
    navigationIcon: @Composable () -> Unit,
    loading: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedBotState by remember { mutableStateOf(null as BotState?) }
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    LaunchedEffect(Unit) {
        snapshotFlow { drawerState.isClosed }
            .filter { it }
            .collect {
                selectedBotState = null
            }
    }

    val content = @Composable {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.bot_title)) },
                    backgroundColor = MaterialTheme.colors.surface,
                    navigationIcon = navigationIcon
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onCreateBot,
                    backgroundColor = MaterialTheme.colors.surface,
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            },
        ) {
            BotList(bots = botStates, onClickBot = {
                selectedBotState = it
                coroutineScope.launch {
                    drawerState.open()
                }
            })
        }
    }

    val drawerContent:@Composable ColumnScope.() -> Unit = @Composable {
        val botPhase = selectedBotState?.phase
        if (botPhase != null) {
            OperateBotSelectionList(
                botPhase = botPhase,
                onEditBotSelected = {
                    coroutineScope.launch { drawerState.close() }
                    selectedBotState?.let(onEditBotState)
                },
                onRemoveBotSelected = {
                    selectedBotState?.let(onRemoveBotState)
                    selectedBotState = null
                    coroutineScope.launch { drawerState.close() }
                },
                onReLoginBotSelected = {
                    selectedBotState?.let(onReLoginBotState)
                    selectedBotState = null
                    coroutineScope.launch { drawerState.close() }
                },
                onLogoutBotSelected = {
                    selectedBotState?.let(onCloseBotState)
                    selectedBotState = null
                    coroutineScope.launch { drawerState.close() }
                },
                onLoginBotSelected = {
                    selectedBotState?.let(onLoginBotState)
                    selectedBotState = null
                    coroutineScope.launch { drawerState.close() }
                },
            )
        } else {
            AddBotSelectionList(
                onAddBotSelected = {
                    coroutineScope.launch { drawerState.close() }
                    onCreateBot()
                }
            )
        }
    }

    BottomDrawerFix(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = drawerContent,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun BotScreenPreview() {
    val fakeBots = mutableListOf<Bot>()
    for (i in 1..5) {
        fakeBots.add(FakeBot())
    }
    Surface(
        modifier = Modifier
            .height(1024.dp)
            .width(768.dp)
    ) {
        // BotScreen(fakeBots, {})
    }
}