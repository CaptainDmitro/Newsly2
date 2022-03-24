package org.captaindmitro.newsly2.ui

import androidx.compose.runtime.Composable
import org.captaindmitro.newsly2.navigation.NavScreen
import org.captaindmitro.newsly2.ui.theme.Newsly2Theme

@Composable
fun MainScreen() {
    Newsly2Theme {
        NavScreen()
    }
}