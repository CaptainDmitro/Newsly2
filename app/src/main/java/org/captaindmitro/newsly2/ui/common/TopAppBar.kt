package org.captaindmitro.newsly2.ui.common

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.example.newsly2.R

@Composable
fun TopAppBar(
    currentQuery: String,
    search: (String) -> Unit,
    navToFavorites: () -> Unit,
    openDrawer: () -> Unit
) {
    TopAppBar(
        title = { Text("${stringResource(id = R.string.app_name)}: ${currentQuery.replaceFirstChar { it.uppercase() }}") },
        actions = { ActionsBar(search, navToFavorites) },
        navigationIcon = { IconButton(onClick = openDrawer) { Icon(Icons.Default.Menu, "") } },
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}

@Composable
fun ActionsBar(
    search: (String) -> Unit,
    navToFavorites: () -> Unit
) {
    var isSearchExpanded by remember { mutableStateOf(false) }
    val onExpand: (Boolean) -> Unit = { isSearchExpanded = !isSearchExpanded }

    if (!isSearchExpanded) {
        IconButton(onClick = navToFavorites) {
            Icon(Icons.Default.Favorite, "")
        }
    }

    SearchBar(onSubmit = search, isExpanded = isSearchExpanded, onExpand = onExpand)
}