package com.example.newsly2.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsly2.ui.home.HomeScreen
import com.example.newsly2.ui.home.HomeViewModel
import com.example.newsly2.ui.details.NewDetailsScreen
import com.example.newsly2.ui.favorites.FavoriteScreen

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
        composable(
            "${NavDestination.DETAILS}/{${NavDestination.URL_KEY}}",
            arguments = listOf(navArgument(NavDestination.URL_KEY) { type = NavType.StringType })
        ) {
            NewDetailsScreen(
                navController = navController,
                url = it.arguments?.getString(NavDestination.URL_KEY)!!
            )
        }
        composable(NavDestination.FAVORITE) {
            FavoriteScreen(homeViewModel = homeViewModel)
        }
    }
}