package org.captaindmitro.newsly2.ui.details

import androidx.compose.runtime.Composable
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import org.captaindmitro.newsly2.utils.navUnmaskUrl

@Composable
fun NewDetailsScreen(
    url: String
) {
    val unMaskUrl = navUnmaskUrl(url)
    val state = rememberWebViewState(url = unMaskUrl)
    WebView(state = state)
}