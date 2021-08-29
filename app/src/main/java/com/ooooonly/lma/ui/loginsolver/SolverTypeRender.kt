package com.ooooonly.lma.ui.loginsolver

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.ooooonly.lma.utils.zoom
import net.mamoe.mirai.Bot

@Composable
fun SolverTypeRender(
    solverType: LoginSolverType,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(5.dp)
    ) {
        when(solverType) {
            is LoginSolverType.PicCaptcha -> {
                Column {
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(solverType.data, 0 , solverType.data.size).zoom(3.0f).asImageBitmap(),
                        contentDescription = null,
                    )
                }
            }
            is LoginSolverType.SliderCaptcha -> {
                Text("滑块验证(暂不支持)：${solverType.url}")
            }
            is LoginSolverType.UnsafeDeviceLoginVerify -> {
                Text("设备锁验证(暂不支持)：${solverType.url}")
            }
        }
    }
}

sealed class LoginSolverType(val bot: Bot) {
    class PicCaptcha(bot: Bot, val data: ByteArray): LoginSolverType(bot)
    class SliderCaptcha(bot: Bot, val url: String): LoginSolverType(bot)
    class UnsafeDeviceLoginVerify(bot: Bot, val url: String): LoginSolverType(bot)
}