package com.example.countriesgame.ui.gamescreen.state

import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.User

sealed class GameScreenUiState {

    object Loading : GameScreenUiState()

    data class RoundFinished(
        val user1: User,
        val user2: User,
        val resultBackgroundColor: Color = Color.LightGray,
        val resultText: String = "Result: Draw",
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
        val user1: User,
        val user2: User,
        val currentLetter: Char = ' ',
        val countriesRemaining: List<Country> = emptyList(),
        val numOfCountriesLeft: Int = 100,
        val player1TurnColor: Color = Color.LightGray,
        val player2TurnColor: Color = Color.LightGray,
        val searchBarText: String = "",
        val remainingLetters: List<Char> = emptyList(),
    ) : GameScreenUiState()
}