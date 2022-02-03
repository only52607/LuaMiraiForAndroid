package com.ooooonly.lma.ui.bot.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.R
import com.ooooonly.lma.datastore.entity.BotEntity
import com.ooooonly.lma.ui.components.FormItem
import com.ooooonly.lma.ui.components.MiraiProtocolDropDown
import com.ooooonly.lma.utils.ripperClickable
import com.ooooonly.lma.utils.toJsonString
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.DeviceInfo

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BotParamsForm(
    botParamState: BotParamState = rememberBotParamState()
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = botParamState.id,
            onValueChange = botParamState::onChangeId,
            label = { Text(stringResource(R.string.bot_create_form_id)) },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            value = botParamState.password,
            onValueChange = botParamState::onChangePassword,
            label = { Text(stringResource(R.string.bot_create_form_password)) },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        FormItem(stringResource(R.string.bot_create_form_protocol)) {
            MiraiProtocolDropDown(
                selectedProtocol = botParamState.protocol,
                onChangeProtocol = botParamState::onChangeProtocol
            )
        }
        Spacer(Modifier.height(10.dp))
        ListItem(
            modifier = Modifier.ripperClickable { botParamState.onChangeAutoLogin(!botParamState.autoLogin) },
            icon = {
                Switch(botParamState.autoLogin, onCheckedChange = botParamState::onChangeAutoLogin)
            }
        ) {
            Text(stringResource(R.string.bot_create_form_auto_login), style = MaterialTheme.typography.subtitle1)
        }
        Spacer(Modifier.height(15.dp))
        OutlinedTextField(
            value = botParamState.device,
            onValueChange = botParamState::onChangeDevice,
            label = { Text(stringResource(R.string.bot_create_form_device_info)) },
            modifier = Modifier.fillMaxWidth().weight(1f),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
        )
        Spacer(Modifier.height(15.dp))
        TextButton(onClick = botParamState::setRandomDeviceInfo) {
            Text(stringResource(R.string.bot_create_form_device_info_generate))
        }
    }
}

class BotParamState(
    initialId: String = "",
    initialPassword: String = "",
    initialDevice: String = DeviceInfo.random().toJsonString(),
    initialProtocol: BotConfiguration.MiraiProtocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE,
    initialAutoLogin: Boolean = false,
) {
    var id by mutableStateOf(initialId)
        private set
    var password by mutableStateOf(initialPassword)
        private set
    var device by mutableStateOf(initialDevice)
        private set
    var protocol by mutableStateOf(initialProtocol)
        private set
    var autoLogin by mutableStateOf(initialAutoLogin)
        private set

    fun onChangeId(id: String) {
        kotlin.runCatching {
            this.id = id
        }
    }

    fun onChangePassword(password: String) {
        this.password = password
    }

    fun onChangeDevice(device: String) {
        this.device = device
    }

    fun onChangeProtocol(protocol: BotConfiguration.MiraiProtocol) {
        this.protocol = protocol
    }

    fun onChangeAutoLogin(autoLogin: Boolean) {
        this.autoLogin = autoLogin
    }

    fun setRandomDeviceInfo() {
        this.device = DeviceInfo.random().toJsonString()
    }
}

@Composable
fun rememberBotParamState(botParam: BotEntity? = null) = remember {
    if (botParam == null) return@remember BotParamState()
    BotParamState(
        initialId = botParam.id.toString(),
        initialDevice = try { botParam.deviceInfo.toJsonString() } catch (e:Exception) { DeviceInfo.random().toJsonString() },
        initialPassword = botParam.password,
        initialAutoLogin = botParam.autoLogin,
        initialProtocol = botParam.protocol
    )
}