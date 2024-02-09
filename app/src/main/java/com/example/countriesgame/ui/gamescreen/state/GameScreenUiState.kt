package com.example.countriesgame.ui.gamescreen.state

import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.RoundResult

sealed class GameScreenUiState {

    object Loading : GameScreenUiState()

    data class RoundFinished(
        val player1Name: String = "Player 1",
        val player2Name: String = "Player 2",
        val resultBackgroundColor: Color = Color.LightGray,
        val result: String = "Result: Draw",
        val currentLetter: Char = ' ',
        val missedCountries: List<Country> = emptyList(),
        val numOfMissedCountries: Int = 100,
        val player1Score: Int = 0,
        val player2Score: Int = 0,
        val isPlayer1Turn: Boolean = true,
        val remainingLetters: List<Char> = emptyList(),
        val roundResult: RoundResult = RoundResult.Draw,
    ) : GameScreenUiState()

    data class GameOver(
        val winner: String,
    ) : GameScreenUiState()

    data class RoundInProgress(
        val player1Name: String = "Player 1",
        val player2Name: String = "Player 2",
        val currentLetter: Char = ' ',
        val showCountriesRemaining: Boolean = false,
        val countriesRemaining: List<Country> = emptyList(),
        val numOfCountriesLeft: Int = 100,
        val player1Score: Int = 0,
        val player2Score: Int = 0,
        val player1Countries: List<String> = emptyList(),
        val player2Countries: List<String> = emptyList(),
        val player1TurnColor: Color = Color.LightGray,
        val player2TurnColor: Color = Color.LightGray,
        val isPlayer1Turn: Boolean = true,
        val searchBarText: String = "",
        val remainingLetters: List<Char> = emptyList(),
    ) : GameScreenUiState()
}