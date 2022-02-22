package com.example.newsly2.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDirections
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsly2.ui.home.DetailsScreen
import com.example.newsly2.ui.home.HomeScreen
import com.example.newsly2.ui.home.HomeViewModel

@Composable
fun NavScreen() {
    val navController = rememberNavController()
    val homeViewModel = hiltViewModel<HomeViewModel>()

    NavHost(navController = navController, startDestination = NavDestination.HOME) {
        composable(NavDestination.HOME) {
            HomeScreen(
                homeViewModel = homeViewModel,
                navController = navController
            )
        }
        composable(NavDestination.DETAILS) {
            DetailsScreen(homeViewModel = homeViewModel)
        }
    }
}