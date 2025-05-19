package com.example.namibiahockeyapp.navigation

import HomeOriginalScreen
import com.example.namibiahockeyapp.ui.screens.LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.namibiahockeyapp.ui.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object OTPVerification : Screen("OTP_verification")
    object Registration : Screen("registration")
    object HomeOriginal : Screen("home_original")
    object PlayerRegistration : Screen("player_registration")
    object TeamRegistration : Screen("team_registration")
    object EventEntry : Screen("event_entry")
    object EventList : Screen("event_list")
    object Announcements : Screen("announcements")
    object PlayerManagement : Screen("player_management")

}

@Composable
fun AppNavigation(navController: NavHostController) {
    val savedEvents = remember { mutableStateListOf<String>() }

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }

        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate(Screen.HomeOriginal.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.EventList.route) {
            EventListScreen(navController, savedEvents)
        }

        composable(Screen.EventEntry.route) {
            EventEntryScreen(navController) { event ->
                savedEvents.add(event)
                navController.navigate(Screen.EventList.route)
            }
        }

        composable(Screen.HomeOriginal.route) { HomeOriginalScreen(navController) }
        composable(Screen.OTPVerification.route) { OTPVerificationScreen(navController) }
        composable(Screen.ForgotPassword.route) { ForgotPasswordScreen(navController) }
        composable(Screen.Registration.route) { RegistrationScreen(navController) }
        composable(Screen.TeamRegistration.route) { TeamRegistrationScreen(navController) }
        composable(Screen.PlayerRegistration.route) { PlayerRegistrationScreen(navController) }
        composable(Screen.Announcements.route) { AnnouncementsScreen(navController) }
        composable(Screen.PlayerManagement.route) { PlayerManagementScreen(navController) }
    }
}

