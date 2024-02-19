package com.example.countriesgame.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.countriesgame.ui.gamescreen.GameScreen

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "game_screen") {
        composable("game_screen") {
            GameScreen()
        }
    }
}