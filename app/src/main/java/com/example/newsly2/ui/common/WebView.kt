package com.example.newsly2.ui.common

import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebView(url: String) {
    val context = LocalContext.current

    AndroidView(factory = {
        android.webkit.WebView(context).apply {
            webViewClient = WebViewClient()

            loadUrl(url)
        }
    })
}