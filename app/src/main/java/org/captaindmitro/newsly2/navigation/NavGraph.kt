package org.captaindmitro.newsly2.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.captaindmitro.newsly2.ui.details.NewDetailsScreen
import org.captaindmitro.newsly2.ui.favorites.FavoriteScreen
import org.captaindmitro.newsly2.ui.home.HomeScreen
import org.captaindmitro.newsly2.ui.home.HomeViewModel
import org.captaindmitro.newsly2.ui.login.AuthScreen
import org.captaindmitro.newsly2.ui.login.AuthViewModel

@Composable
fun NavScreen(
    navController: NavHostController
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val authViewModel = hiltViewModel<AuthViewModel>()

    NavHost(navController = navController, startDestination = NavDestination.AUTH) {
        composable(NavDestination.AUTH) {
            AuthScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("${NavDestination.HOME}/{userName}") {
            HomeScreen(
                homeViewModel = homeViewModel,
                authViewModel = authViewModel,
                navController = navController,
                it.arguments?.getString("userName")
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