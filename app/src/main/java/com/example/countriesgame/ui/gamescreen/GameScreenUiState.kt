package com.example.countriesgame.ui.gamescreen

import androidx.compose.ui.graphics.Color

sealed class GameScreenUiState {
    object Loading : GameScreenUiState()
    object RoundFinished : GameScreenUiState()
    object GameOver : GameScreenUiState()
    data class GameInProgress(
        val player1Name: String = "Player 1",
        val player2Name: String = "Player 2",
        val currentLetter: Char = ' ',
        val showCountriesRemaining: Boolean = false,
        val countriesRemaining: List<String> = emptyList(),
        val numOfCountriesLeft: Int = 100,
        val player1Score: Int = 0,
        val player2Score: Int = 0,
        val player1Countries: List<String> = emptyList(),
        val player2Countries: List<String> = emptyList(),
        val player1TurnColor: Color = Color.White,
        val player2TurnColor: Color = Color.White,
        val isPlayer1Turn: Boolean = true,
        val keyboardText: String = "",
        val remainingLetters: List<Char> = listOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l','m', 'n', 'o', 'p', 'q', 'r','s', 't', 'u', 'v', 'y', 'z')
    ) : GameScreenUiState()
}