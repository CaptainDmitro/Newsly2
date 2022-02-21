package com.example.newsly2.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsly2.ui.details.DetainsScreen
import com.example.newsly2.ui.theme.Newsly2Theme
import com.example.newsly2.utils.fromCategory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Newsly2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    HomeScreen(homeViewModel)
                    NavScreen(homeViewModel = homeViewModel)
                }
            }
        }
    }
}

@Composable
fun NavScreen(
    homeViewModel: HomeViewModel,

) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = homeViewModel,
                navController = navController
            )
        }
        composable("details/{title}") { navBackStackEntry ->  DetainsScreen(navBackStackEntry.arguments?.getString("title") ?: "kek") }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val news = viewModel.news.collectAsState()
    val updateCategory = viewModel::updateCategory

    Scaffold(
        modifier = Modifier
            .padding(6.dp)
    ) {
        Column {
            CategoriesList(categories = fromCategory.keys.toList(), onClick = updateCategory)
            NewsList(news = news.value,  navController = navController, modifier = Modifier.padding(it))
        }
    }
}