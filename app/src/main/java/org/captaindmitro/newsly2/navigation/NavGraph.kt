package org.captaindmitro.newsly2.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import org.captaindmitro.newsly2.ui.details.NewDetailsScreen
import org.captaindmitro.newsly2.ui.favorites.FavoriteScreen
import org.captaindmitro.newsly2.ui.home.HomeScreen
import org.captaindmitro.newsly2.ui.home.HomeViewModel
import org.captaindmitro.newsly2.ui.login.AuthScreen
import org.captaindmitro.newsly2.ui.login.AuthViewModel
import org.captaindmitro.newsly2.utils.navMaskUrl

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    AnimatedNavHost(
        navController = navController,
        startDestination = NavDestination.AUTH,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 })
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 })
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 })
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 })
        },
        modifier = Modifier.then(modifier)
    ) {
        composable(route = NavDestination.AUTH) {
            AuthScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(route = "${NavDestination.HOME}/{userName}") {
            HomeScreen(
                homeViewModel = homeViewModel,
                authViewModel = authViewModel,
                navController = navController,
                onUpdateCategory = homeViewModel::changeCategory,
                onClickDetails = { url ->
                    val maskUrl = navMaskUrl(url)
                    navController.navigate("${NavDestination.DETAILS}/$maskUrl")
                },
                onChangeLanguage = homeViewModel::changeLanguage,
                onFavoriteArticle = homeViewModel::likeArticle,
                onNavToFavorites = {
                    navController.navigate(NavDestination.FAVORITE)
                },
                isLiked = homeViewModel::isArticleLiked,
                userName = it.arguments?.getString("userName")
            )
        }
        composable(
            "${NavDestination.DETAILS}/{${NavDestination.URL_KEY}}",
            arguments = listOf(navArgument(NavDestination.URL_KEY) { type = NavType.StringType })
        ) {
            NewDetailsScreen(
                url = it.arguments?.getString(NavDestination.URL_KEY)!!
            )
        }
        composable(NavDestination.FAVORITE) {
            FavoriteScreen(homeViewModel = homeViewModel)
        }
    }
}