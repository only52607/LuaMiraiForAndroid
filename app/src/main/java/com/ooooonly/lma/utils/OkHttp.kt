package com.ooooonly.lma.utils

import okhttp3.Request

fun buildRequest(builder: Request.Builder.() -> Unit): Request {
    return Request.Builder().apply(builder).build()
}