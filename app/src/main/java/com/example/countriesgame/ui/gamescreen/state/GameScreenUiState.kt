package com.example.countriesgame.ui.gamescreen.state

import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.Player

sealed class GameScreenUiState {

    object Loading : GameScreenUiState()

    data class RoundFinished(
        val player1: Player,
        val player2: Player,
        val resultBackgroundColor: Color = Color.LightGray,
        val result: String = "Result: Draw",
        val currentLetter: Char = ' ',
        val missedCountries: List<Country> = emptyList(),
        val numOfMissedCountries: Int = 100,
        //val isPlayer1Turn: Boolean = true,
        val remainingLetters: List<Char> = emptyList(),
    ) : GameScreenUiState()

    data class GameOver(
        val winner: String,
    ) : GameScreenUiState()

    data class RoundInProgress(
        val player1: Player,
        val player2: Player,
        val currentLetter: Char = ' ',
        val countriesRemaining: List<Country> = emptyList(),
        val numOfCountriesLeft: Int = 100,
        val player1TurnColor: Color = Color.LightGray,
        val player2TurnColor: Color = Color.LightGray,
        val searchBarText: String = "",
        val remainingLetters: List<Char> = emptyList(),
    ) : GameScreenUiState()
}