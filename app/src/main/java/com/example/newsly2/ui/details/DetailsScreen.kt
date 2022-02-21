package com.example.newsly2.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.newsly2.model.Article

@Composable
fun DetainsScreen(
    title: String,
    //content: String
) {
    Column {
        Text(title)
        //Text(content)
    }
}