package com.ooooonly.lma.ui.loginsolver

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.ooooonly.lma.utils.ripperClickable
import com.ooooonly.lma.utils.zoom

@Composable
fun PicCaptchaImage(
    data: ByteArray,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.ripperClickable { onClick() }) {
        Image(
            bitmap = BitmapFactory.decodeByteArray(
                data,
                0,
                data.size
            ).zoom(3.0f).asImageBitmap(),
            contentDescription = null,
        )
    }
}