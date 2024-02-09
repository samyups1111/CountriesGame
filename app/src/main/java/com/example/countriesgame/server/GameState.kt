package com.example.countriesgame.server

import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.RoundResult
import com.example.countriesgame.ui.gamescreen.state.GameScreenUiState

sealed class GameState {

    object Loading : GameState()

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
    ) : GameState()

    data class GameOver(
        val winner: String,
    ) : GameState()

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
    ) : GameState()
}

fun GameState.toGameScreenUiState(): GameScreenUiState {
    return when (this) {
        is GameState.GameOver -> {
            GameScreenUiState.GameOver(
                winner = this.winner,
            )
        }
        is GameState.Loading -> {
            GameScreenUiState.Loading
        }
        is GameState.RoundInProgress -> {
            GameScreenUiState.RoundInProgress(
                player1Name = this.player1Name,
                player2Name = this.player2Name,
                currentLetter = this.currentLetter,
                showCountriesRemaining = this.showCountriesRemaining,
                countriesRemaining = this.countriesRemaining,
                numOfCountriesLeft = this.numOfCountriesLeft,
                player1Score = this.player1Score,
                player2Score = this.player2Score,
                player1Countries = this.player1Countries,
                player2Countries = this.player2Countries,
                player1TurnColor = this.player1TurnColor,
                player2TurnColor = this.player2TurnColor,
                isPlayer1Turn = this.isPlayer1Turn,
                searchBarText = this.searchBarText,
                remainingLetters = this.remainingLetters,
            )
        }
        is GameState.RoundFinished -> {
            GameScreenUiState.RoundFinished(
                player1Name = this.player1Name,
                player2Name = this.player2Name,
                resultBackgroundColor = this.resultBackgroundColor,
                result = this.result,
                currentLetter = this.currentLetter,
                missedCountries = this.missedCountries,
                numOfMissedCountries = this.numOfMissedCountries,
                player1Score = this.player1Score,
                player2Score = this.player2Score,
                isPlayer1Turn = this.isPlayer1Turn,
                remainingLetters = this.remainingLetters,
                roundResult = this.roundResult,
            )
        }
    }
}
