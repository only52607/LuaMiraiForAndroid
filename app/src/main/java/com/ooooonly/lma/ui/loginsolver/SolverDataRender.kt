package com.ooooonly.lma.ui.loginsolver

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.utils.ripperClickable
import net.mamoe.mirai.Bot
import com.ooooonly.lma.R

@Composable
fun SolverDataRender(
    solverState: SolverState,
    solverData: LoginSolverData,
    onUpdate: () -> Unit,
    onSolved: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Box(
        modifier = Modifier.padding(5.dp)
    ) {
        when (solverData) {
            is LoginSolverData.PicCaptcha -> {
                Column {
                    PicCaptchaImage(data = solverData.data, onClick = onUpdate)
                    TextField(
                        maxLines = 1,
                        value = solverState.result,
                        onValueChange = { solverState.result = it },
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                    )
                }
            }
            is LoginSolverData.SliderCaptcha -> {
                SliderCaptchaWebView(url = solverData.url, onDone = {
                    Log.d("login", "got ticket $it")
                    onSolved(it)
                })
            }
            is LoginSolverData.UnsafeDeviceLoginVerify -> {
                Box(modifier = Modifier.ripperClickable {
                    clipboardManager.setText(AnnotatedString(text = solverData.url))
                    Toast.makeText(context, stringResource(R.string.copy_to_clip_board_success), Toast.LENGTH_SHORT).show()
                }) {
                    Text("${stringResource(R.string.login_solver_device_title)}：${solverData.url}")
                }
            }
        }
    }
}

class SolverState {
    var result by mutableStateOf("")
}

@Composable
fun rememberSolverState() = remember {
    SolverState()
}

sealed class LoginSolverData(val bot: Bot) {
    class PicCaptcha(bot: Bot, val data: ByteArray) : LoginSolverData(bot)
    class SliderCaptcha(bot: Bot, val url: String) : LoginSolverData(bot)
    class UnsafeDeviceLoginVerify(bot: Bot, val url: String) : LoginSolverData(bot)
}