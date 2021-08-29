package com.ooooonly.lma.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable

fun Bitmap.zoom(x: Float, y: Float): Bitmap {
    val w = this.width
    val h = this.height
    val matrix = Matrix()
    matrix.postScale(x, y)
    return Bitmap.createBitmap(
        this, 0, 0, w,
        h, matrix, true
    )
}

fun Bitmap.zoom(ratio: Float): Bitmap = zoom(ratio, ratio)

fun Drawable.asBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        this.intrinsicWidth, this.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}