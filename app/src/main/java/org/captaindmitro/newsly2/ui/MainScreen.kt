package org.captaindmitro.newsly2.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch
import org.captaindmitro.newsly2.navigation.NavDestination
import org.captaindmitro.newsly2.navigation.NavScreen
import org.captaindmitro.newsly2.ui.theme.Newsly2Theme
import org.captaindmitro.newsly2.utils.fromLanguage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberAnimatedNavController()
) {
    Newsly2Theme {
        Scaffold { paddingValues ->
            NavScreen(navController, modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun Fab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        Icon(Icons.Default.KeyboardArrowUp, "")
    }
}

@Composable
fun Drawer(
    userName: String?,
    onLogOut: () -> Unit,
    onLanguageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by mutableStateOf(false)
    var selectedText by remember { mutableStateOf("English") }

    Surface(
        color = MaterialTheme.colors.primarySurface
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 48.dp)
        ) {
            Text("News")
            Text("Favorites")
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedText)
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    fromLanguage.keys.forEach { lang ->
                        DropdownMenuItem(
                            onClick = { selectedText = lang; expanded = false; onLanguageChange(
                            fromLanguage[lang] ?: throw Exception("Unsupported language")) }) {
                            Text(lang)
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp)) {
            Text(text = userName!!)
            OutlinedButton(onClick = onLogOut) {
                Text("Logout")
            }
        }

    }
}