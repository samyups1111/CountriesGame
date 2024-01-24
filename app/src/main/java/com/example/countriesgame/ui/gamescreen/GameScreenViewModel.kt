package com.example.countriesgame.ui.gamescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.countriesgame.model.GetCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
): ViewModel() {

    var gameScreenUiState by mutableStateOf<GameScreenUiState>(GameScreenUiState.Loading)
        private set

    init {
        startGame()
    }

    private fun startGame() {

        val currentLetter = GameScreenUiState.GameInProgress().remainingLetters.random()
        val countriesRemaining = getCountriesUseCase.invoke(letter = currentLetter)

        gameScreenUiState = GameScreenUiState.GameInProgress(
            currentLetter = currentLetter,
            countriesRemaining = countriesRemaining,
            numOfCountriesLeft = countriesRemaining.size,
            player1TurnColor = Color.Green,
        )
    }

    fun onCountryGuessed(country: String) {

        val state = gameScreenUiState as GameScreenUiState.GameInProgress
        updateKeyboard(country)

        state.countriesRemaining.forEach {
            if (it.equals(country.trim(), ignoreCase = true)) {
                onGuessedCorrectly(it)
            }
        }
    }

    private fun updateKeyboard(country: String) {

        val state = gameScreenUiState as GameScreenUiState.GameInProgress

        gameScreenUiState = state.copy(
            showCountriesRemaining = false,
            keyboardText = country,
        )
    }

    private fun onGuessedCorrectly(country: String) {

        var state = gameScreenUiState as GameScreenUiState.GameInProgress

        gameScreenUiState = state.copy(
            countriesRemaining = state.countriesRemaining.minusElement(country),
            player1Countries = addCountryToCorrectlyGuessedList(country = country, Players.Player1),
            player2Countries = addCountryToCorrectlyGuessedList(country = country, Players.Player2),
            keyboardText = "",
            numOfCountriesLeft = state.numOfCountriesLeft - 1,
            isPlayer1Turn = !state.isPlayer1Turn,
            player1TurnColor = getTurnColor(
                isPlayer1Turn = !state.isPlayer1Turn,
                player = Players.Player1,
            ),
            player2TurnColor = getTurnColor(
                isPlayer1Turn = !state.isPlayer1Turn,
                player = Players.Player2,
            ),
        )

        state = gameScreenUiState as GameScreenUiState.GameInProgress

        if (state.countriesRemaining.isEmpty()) {
            if (state.remainingLetters.isEmpty()) {
                // Game over
                gameScreenUiState = GameScreenUiState.GameOver
            } else {
                loadNextLetter()
            }
        }
    }

    private fun getTurnColor(isPlayer1Turn: Boolean, player: Players): Color {
        return when (player) {
            is Players.Player1 -> {
                if (isPlayer1Turn) Color.Green else Color.White
            }
            is Players.Player2 -> {
                if (isPlayer1Turn) Color.White else Color.Green
            }
        }
    }

    private fun addCountryToCorrectlyGuessedList(country: String, player: Players): List<String> {

        val state = gameScreenUiState as GameScreenUiState.GameInProgress

        return when (player) {
            is Players.Player1 -> {
                if (state.isPlayer1Turn) {
                    val currentList = state.player1Countries.toMutableStateList()
                    currentList.add(country)
                    currentList.toList()
                } else {
                    state.player1Countries
                }
            }
            is Players.Player2 -> {
                if (state.isPlayer1Turn) {
                    state.player2Countries
                } else {
                    val currentList = state.player2Countries.toMutableStateList()
                    currentList.add(country)
                    currentList.toList()                }
            }
        }
    }

    fun updateStateOnGiveUp() {
        val state = gameScreenUiState as GameScreenUiState.GameInProgress

        if (state.remainingLetters.isEmpty()) {
            gameScreenUiState = GameScreenUiState.GameOver
        } else {
            val currentLetter = state.remainingLetters.random()
            val countriesRemaining = getCountriesUseCase.invoke(currentLetter)
            val numOfCountriesLeft = countriesRemaining.size

            gameScreenUiState = state.copy(
                currentLetter = currentLetter,
                countriesRemaining = countriesRemaining,
                numOfCountriesLeft = numOfCountriesLeft,
                player1Countries = emptyList(),
                player2Countries = emptyList(),
                remainingLetters = state.remainingLetters.filter { it != currentLetter },
                showCountriesRemaining = true,
                player1Score = if (state.isPlayer1Turn) state.player1Score else state.player1Score + 1,
                player2Score = if (state.isPlayer1Turn) state.player2Score + 1 else state.player2Score,
                isPlayer1Turn = !state.isPlayer1Turn,
                player1TurnColor = getTurnColor(isPlayer1Turn = !state.isPlayer1Turn, player = Players.Player1),
                player2TurnColor = getTurnColor(isPlayer1Turn = !state.isPlayer1Turn, player = Players.Player2),
            )
        }
    }

    private fun loadNextLetter() {
        val state = gameScreenUiState as GameScreenUiState.GameInProgress

        val currentLetter = state.remainingLetters.random()
        val countriesRemaining = getCountriesUseCase.invoke(currentLetter)
        val numOfCountriesLeft = countriesRemaining.size

        gameScreenUiState = state.copy(
            currentLetter = currentLetter,
            countriesRemaining = countriesRemaining,
            numOfCountriesLeft = numOfCountriesLeft,
            player1Countries = emptyList(),
            player2Countries = emptyList(),
            remainingLetters = state.remainingLetters.filter { it != currentLetter }
        )
    }
}