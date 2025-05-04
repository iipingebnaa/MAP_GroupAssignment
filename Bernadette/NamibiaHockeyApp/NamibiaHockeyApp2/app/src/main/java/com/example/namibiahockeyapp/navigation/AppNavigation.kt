package com.example.namibiahockeyapp.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.namibiahockeyapp.ui.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object Registration : Screen("registration")
    object PlayerRegistration : Screen("player_registration")
    object TeamRegistration : Screen("team_registration")
    object EventEntry : Screen("event_entry")
    object Announcements : Screen("announcements")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }

        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Registration.route) { RegistrationScreen(navController) }
        composable(Screen.TeamRegistration.route) { TeamRegistrationScreen(navController) }
        composable(Screen.PlayerRegistration.route) { PlayerRegistrationScreen(navController) }
        composable(Screen.EventEntry.route) { EventEntryScreen(navController) }
        composable(Screen.Announcements.route) { AnnouncementsScreen(navController) }
    }
}

