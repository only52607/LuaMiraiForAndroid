package com.ooooonly.lma.ui.loginsolver

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val sliderCaptchaWebViewHeight = 270.dp

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SliderCaptchaWebView(
    url: String,
    onDone: (String) -> Unit
) {
    AndroidView(
        factory = {
            WebView(it).also { webView ->
                webView.webViewClient = TicketCaptureWebViewClient(onCaptured = onDone)
                WebView.setWebContentsDebuggingEnabled(true)
                webView.settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                }
                webView.loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxWidth().height(sliderCaptchaWebViewHeight),
        update = {
            it.loadUrl(url)
        }
    )
}

private class TicketCaptureWebViewClient(
    val onCaptured: (String) -> Unit
): WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        if (request == null) return false
        if (request.url.path.equals("/onVerifyCAPTCHA")) {
            val p = request.url.getQueryParameter("p") ?: return false
            val ticket =
                Json.parseToJsonElement(p).jsonObject["ticket"]?.jsonPrimitive?.content
                    ?: return false
            onCaptured(ticket)
        }
        return false
    }
}