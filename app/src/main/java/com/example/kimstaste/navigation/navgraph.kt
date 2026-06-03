package com.example.kimstaste.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kimstaste.ui.screens.*

@Composable
fun KimstasteNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            Splashscreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }

        composable(Screen.Home.route) {
            Homescreen(navController)
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        
        composable(Screen.MemberCard.route) {
            // We can retrieve the name from previous backstack entry if passed via savedStateHandle
            // or just use a default for now. A better way is to pass it as an argument.
            val name = navController.previousBackStackEntry?.savedStateHandle?.get<String>("userName") ?: "Kims Guest"
            MemberCardScreen(navController, name)
        }

        composable(
            route = Screen.UploadMedia.route + "/{mediaId}",
            arguments = listOf(
                navArgument("mediaId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mediaId = backStackEntry.arguments?.getString("mediaId") ?: ""
            // MediaDetailScreen(navController, mediaId)
        }

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
