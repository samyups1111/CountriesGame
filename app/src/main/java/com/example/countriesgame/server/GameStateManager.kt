package com.example.countriesgame.server

import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GameStateManager @Inject constructor() {
    var gameState : MutableStateFlow<GameState> = MutableStateFlow(GameState.Loading)
        private set
    var prevState = GameState.RoundInProgress()
    private var gameInProgressState = GameState.RoundInProgress()

    fun setStartState(
        startingLetter: Char,
        countriesRemaining: List<Country>,
        remainingLetters: List<Char>,
    ) {
        val startState = GameState.RoundInProgress(
            currentLetter = startingLetter,
            countriesRemaining = countriesRemaining,
            numOfCountriesLeft = countriesRemaining.size,
            player1TurnColor = Color.Yellow,
            remainingLetters = remainingLetters,
        )
        gameState.value = startState
    }

    fun updateSearchBarState(text: String) {
        gameState.value = prevState.copy(searchBarText = text)
    }

    fun savePreviousRoundInProgressState() {
        prevState = gameState.value as GameState.RoundInProgress
    }

    fun setRoundFinishedState(
        player1Score: Int,
        player2Score: Int,
        missedCountriesPrevRound: List<Country>,
        currentLetter: Char,
        countriesRemainingThisRound: List<Country>,
        resultBackgroundColor: Color,
        player1TurnColor: Color,
        player2TurnColor: Color,
        remainingLetters: List<Char>,
        isPlayer1Turn: Boolean,
        result: String,
    ) {
        val numOfCountriesRemaining = countriesRemainingThisRound.size
        gameState.value = GameState.RoundFinished(
            player1Name = prevState.player1Name,
            player2Name = prevState.player2Name,
            currentLetter = currentLetter,
            missedCountries = missedCountriesPrevRound,
            player1Score = player1Score,
            player2Score = player2Score,
            numOfMissedCountries = missedCountriesPrevRound.size,
            remainingLetters = remainingLetters,
            isPlayer1Turn = isPlayer1Turn,
            result = result,
            resultBackgroundColor = resultBackgroundColor,
        )
        saveInProgressState(
            prevState = prevState,
            countriesRemaining = countriesRemainingThisRound,
            numOfCountriesRemaining = numOfCountriesRemaining,
            isPlayer1Turn = isPlayer1Turn,
            player1Score = player1Score,
            player2Score = player2Score,
            currentLetter = currentLetter,
            remainingLetters = remainingLetters,
            player1TurnColor = player2TurnColor,
            player2TurnColor = player1TurnColor,
        )
    }

    private fun saveInProgressState(
        prevState: GameState.RoundInProgress,
        countriesRemaining: List<Country>,
        numOfCountriesRemaining: Int,
        isPlayer1Turn: Boolean,
        player1Score: Int,
        player2Score: Int,
        currentLetter: Char,
        remainingLetters: List<Char>,
        player1TurnColor: Color,
        player2TurnColor: Color,
    ) {
        gameInProgressState = prevState.copy(
            countriesRemaining = countriesRemaining,
            player1Score = player1Score,
            player2Score = player2Score,
            player1Countries = emptyList(),
            player2Countries = emptyList(),
            currentLetter = currentLetter,
            searchBarText = "",
            numOfCountriesLeft = numOfCountriesRemaining,
            isPlayer1Turn = isPlayer1Turn,
            player1TurnColor = player1TurnColor,
            player2TurnColor = player2TurnColor,
            remainingLetters = remainingLetters,
        )
    }

    fun continueRoundState(
        countriesRemaining: List<Country>,
        player1Countries: List<String>,
        player2Countries: List<String>,
        numOfCountriesRemaining: Int,
        player1TurnColor: Color,
        player2TurnColor: Color,
        ) {
        gameState.value = prevState.copy(
            countriesRemaining = countriesRemaining,
            player1Countries = player1Countries,
            player2Countries = player2Countries,
            searchBarText = "",
            numOfCountriesLeft = numOfCountriesRemaining,
            isPlayer1Turn = !prevState.isPlayer1Turn,
            player1TurnColor = player1TurnColor,
            player2TurnColor = player2TurnColor,
        )
    }

    fun startNextRound() {
        gameState.value = gameInProgressState
    }

    fun setGameOverState(winner: String) {
        gameState.value = GameState.GameOver(
            winner = winner,
        )
    }
}