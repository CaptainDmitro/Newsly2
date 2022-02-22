package com.example.newsly2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newsly2.navigation.NavScreen
import com.example.newsly2.ui.theme.Newsly2Theme

@Composable
fun MainScreen() {
    Newsly2Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            NavScreen()
        }
    }
}