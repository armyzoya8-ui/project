package com.example.kimstaste.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kimstaste.ui.screens.*

@Composable
fun KimstasteNavGraph(navController: NavController) {
    // NavHost requires a NavHostController
    val navHostController = navController as NavHostController

    NavHost(
        navController = navHostController,
        startDestination = Screen.Splash.route
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            Splashscreen(navController)
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        // Signup Screen
        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }

        // Home Screen
        composable(Screen.Home.route) {
            // Homescreen(navController)
        }

        // Profile Screen
        composable(Screen.Profile.route) {
            // profileScreen(navController)
        }

        // Upload Media Route
        composable(
            route = Screen.UploadMedia.route + "/{mediaId}",
            arguments = listOf(
                navArgument("mediaId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
            // MediaDetailScreen(navController, mediaId)
        }

        // Edit Media Route
        composable(
            route = Screen.EditMedia.route + "/{mediaId}",
            arguments = listOf(
                navArgument("mediaId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
            // EditMediaScreen(navController, mediaId)
        }
    }
}
