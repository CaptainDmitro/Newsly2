package com.example.newsly2.ui.login

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsly2.MainActivity
import com.example.newsly2.navigation.NavDestination
import com.example.newsly2.ui.home.HomeScreen
import com.example.newsly2.ui.home.HomeViewModel
import com.example.newsly2.ui.theme.Newsly2Theme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
internal class AuthScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            val authViewModel = hiltViewModel<AuthViewModel>()
            val homeViewModel = hiltViewModel<HomeViewModel>()
            Newsly2Theme {
                NavHost(navController = navController, startDestination = NavDestination.AUTH) {
                    composable(route = NavDestination.AUTH) {
                        AuthScreen(navController = navController, authViewModel = authViewModel)
                    }
                    composable("${NavDestination.HOME}/{userName}") {
                        HomeScreen(
                            homeViewModel = homeViewModel,
                            navController = navController,
                            it.arguments?.getString("userName")
                        )
                    }
                }
            }
        }
    }

    @Test
    fun loginWithEmailAndPassword() {
        composeRule.onNodeWithTag("LOGIN_TEXTFIELD").performTextInput("test@text.com")
        composeRule.onNodeWithTag("PASSWORD_TEXTFIELD").performTextInput("Password")
        composeRule.onNodeWithText("Login").performClick()
    }

    @Test
    fun loginAsAnonymous() {
        composeRule.onNodeWithText("Continue without registration").performClick()
    }

}