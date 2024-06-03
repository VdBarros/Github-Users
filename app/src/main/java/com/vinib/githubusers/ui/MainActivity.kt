package com.vinib.githubusers.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vinib.githubusers.ui.screeens.UserDetailsScreen
import com.vinib.githubusers.ui.screeens.UserListingScreen
import com.vinib.githubusers.ui.theme.GitHubUsersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            GitHubUsersTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController, startDestination = "userListingScreen"
                ) {
                    composable("userListingScreen") {
                        UserListingScreen {
                            navController.navigate("UserDetailScreen/$it")
                        }
                    }
                    composable("userDetailScreen/{userLogin}") {
                        it.arguments?.getString("userLogin")?.let { userLogin ->
                            UserDetailsScreen(userLogin = userLogin) {
                                navController.popBackStack()
                            }
                        }
                    }
                    composable("favoriteUsersListing") {

                    }
                }
            }
        }
    }
}