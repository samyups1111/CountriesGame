package com.example.countriesgame.server

import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.Player
import com.example.countriesgame.ui.gamescreen.state.GameScreenUiState

sealed class GameState {

    object Loading : GameState()

    data class RoundFinished(
        val player1: Player,
        val player2: Player,
        val currentLetter: Char = ' ',
        val missedCountries: List<Country> = emptyList(),
        val numOfMissedCountries: Int = 100,
        val remainingLetters: List<Char> = emptyList(),
    ) : GameState()

    data class GameOver(
        val winner: Player,
    ) : GameState()

    data class RoundInProgress(
        val player1: Player,
        val player2: Player,
        val currentLetter: Char = ' ',
        val countriesRemaining: List<Country> = emptyList(),
        val numOfCountriesLeft: Int = 100,
        val remainingLetters: List<Char> = emptyList(),
        val currentAnswer: String = "",
    ) : GameState()
}

fun GameState.toGameScreenUiState(): GameScreenUiState {
    return when (this) {
        is GameState.GameOver -> {
            GameScreenUiState.GameOver(
                winner = this.winner.name,
            )
        }
        is GameState.Loading -> {
            GameScreenUiState.Loading
        }
        is GameState.RoundInProgress -> {
            GameScreenUiState.RoundInProgress(
                player1 = player1,
                player2 = player2,
                currentLetter = this.currentLetter,
                countriesRemaining = this.countriesRemaining,
                numOfCountriesLeft = this.numOfCountriesLeft,
                searchBarText = this.currentAnswer,
                remainingLetters = this.remainingLetters,
                player1TurnColor = if (this.player1.isItsTurn) Color.Yellow else Color.LightGray,
                player2TurnColor = if (this.player2.isItsTurn) Color.Yellow else Color.LightGray,
            )
        }
        is GameState.RoundFinished -> {
            GameScreenUiState.RoundFinished(
                player1 = player1,
                player2 = player2,
                currentLetter = this.currentLetter,
                missedCountries = this.missedCountries,
                numOfMissedCountries = this.numOfMissedCountries,
                remainingLetters = this.remainingLetters,
            )
        }
    }
}
