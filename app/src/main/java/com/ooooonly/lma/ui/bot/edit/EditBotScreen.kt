package com.ooooonly.lma.ui.bot.edit

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.ooooonly.lma.R
import com.ooooonly.lma.model.entity.BotEntity
import com.ooooonly.lma.mirai.MiraiViewModel
import com.ooooonly.lma.utils.fromJsonString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.utils.DeviceInfo

@Composable
fun EditBotScreen(
    snackbarHostState: SnackbarHostState,
    miraiViewModel: MiraiViewModel,
    fromBotId: Long?,
    onBack: () -> Unit
) {
    EditBotScreen(
        snackbarHostState = snackbarHostState,
        onFinish = {
            miraiViewModel.saveBotByParameter(it, fromBotId)
            onBack()
        },
        onBack = onBack,
        botParamState = rememberBotParamState(miraiViewModel.botStates.find { it.entity.id == fromBotId }?.entity),
        title = fromBotId?.let { stringResource(R.string.edit_bot_edit_title) } ?: stringResource(R.string.edit_bot_create_title)
    )
}

@Composable
fun EditBotScreen(
    onFinish: (BotEntity) -> Unit,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    botParamState: BotParamState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    title: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        try {
                            onFinish(buildBotConstructionParameter(botParamState))
                        } catch (e: Exception) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(message = e.message?:"", duration = SnackbarDuration.Short)
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Done, contentDescription = null)
                    }
                },
                title = { Text(title) }
            )
        }
    ) {
        BotParamsForm(botParamState = botParamState)
    }
}

fun buildBotConstructionParameter(createBotState: BotParamState): BotEntity {
    return BotEntity(
        id = try { createBotState.id.toLong() } catch (e:Exception) { throw Exception("ID???????????????") },
        password = createBotState.password.also { it.ifEmpty { throw Exception("??????????????????") }},
        protocol = createBotState.protocol,
        autoLogin = createBotState.autoLogin,
        deviceInfo = try { DeviceInfo.fromJsonString(createBotState.device) } catch (e:Exception) { throw Exception("Device???????????????") }
    )
}