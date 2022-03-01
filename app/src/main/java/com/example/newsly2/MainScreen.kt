package com.example.newsly2

import androidx.compose.runtime.Composable
import com.example.newsly2.navigation.NavScreen
import com.example.newsly2.ui.theme.Newsly2Theme

@Composable
fun MainScreen() {
    Newsly2Theme {
        NavScreen()
    }
}