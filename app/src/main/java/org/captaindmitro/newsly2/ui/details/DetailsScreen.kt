package org.captaindmitro.newsly2.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.newsly2.R
import org.captaindmitro.newsly2.ui.common.WebView
import org.captaindmitro.newsly2.utils.navUnmaskUrl

@Composable
fun NewDetailsScreen(
    navController: NavController,
    url: String
) {
    val unMaskUrl = navUnmaskUrl(url)
    
    Column {
        TopAppBar(
            title = { Text(stringResource(id = R.string.app_name)) },
            navigationIcon = { IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, "")
            } },
            backgroundColor = MaterialTheme.colors.primaryVariant
        )
        WebView(unMaskUrl)
    }
}