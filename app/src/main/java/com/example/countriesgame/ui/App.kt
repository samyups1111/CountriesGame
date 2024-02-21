package com.example.countriesgame.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.countriesgame.ui.gamescreen.GameScreen
import com.example.countriesgame.ui.loginscreen.LoginScreen
import com.example.countriesgame.ui.signupscreen.SignUpScreen

@Composable
fun App() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "signup_screen") {

        composable("signup_screen") {
            SignUpScreen(
                goToHomeScreen = { navController.navigate("game_screen") },
                goToLoginScreen = { navController.navigate("login_screen") }
            )
        }

        composable("login_screen") {
            LoginScreen(
                goToHome = { navController.navigate("game_screen") },
                goToSignupScreen = { navController.navigate("signup_screen") }
            )
        }

        composable("game_screen") {
            GameScreen()
        }
    }
}