package com.example.countriesgame.ui.gamescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.GetCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
): ViewModel() {

    var gameScreenUiState by mutableStateOf<GameScreenUiState>(GameScreenUiState.Loading)
        private set

    var bottomSheetState by mutableStateOf<BottomSheetState>(BottomSheetState.Hide)

    private var gameInProgressState = GameScreenUiState.RoundInProgress()

    init {
        startGame()
    }

    private fun startGame() {

        val currentLetter = GameScreenUiState.RoundInProgress().remainingLetters.random()
        val countriesRemaining = getCountriesUseCase.invoke(letter = currentLetter)

        gameScreenUiState = GameScreenUiState.RoundInProgress(
            currentLetter = currentLetter,
            countriesRemaining = countriesRemaining,
            numOfCountriesLeft = countriesRemaining.size,
            player1TurnColor = Color.Green,
        )
    }

    fun onCountryGuessed(countryGuessed: String) {

        val state = gameScreenUiState as GameScreenUiState.RoundInProgress
        updateKeyboard(countryGuessed)

        state.countriesRemaining.forEach { country ->
            val countryGuessedFormatted = countryGuessed.trim()
            if (country.name.equals(countryGuessedFormatted, ignoreCase = true) ||
                (country.altNames?.contains(countryGuessedFormatted) == true)) {
                onGuessedCorrectly(country.name, state)
            }
        }
    }

    private fun updateKeyboard(country: String) {

        val state = gameScreenUiState as GameScreenUiState.RoundInProgress

        gameScreenUiState = state.copy(
            showCountriesRemaining = false,
            keyboardText = country,
        )
    }

    private fun onGuessedCorrectly(countryName: String, state: GameScreenUiState.RoundInProgress) {

        val numOfCountriesLeft = state.numOfCountriesLeft - 1

        gameScreenUiState = state.copy(
            countriesRemaining = state.countriesRemaining.filter { it.name != countryName },
            player1Countries = addCountryToCorrectlyGuessedList(country = countryName, Players.Player1),
            player2Countries = addCountryToCorrectlyGuessedList(country = countryName, Players.Player2),
            keyboardText = "",
            numOfCountriesLeft = numOfCountriesLeft,
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

        val newState = gameScreenUiState as GameScreenUiState.RoundInProgress

        if (numOfCountriesLeft < 1) {
            if (newState.remainingLetters.isEmpty()) {
                // Game over
                gameScreenUiState = GameScreenUiState.GameOver
            } else {
                gameInProgressState = newState
                setRoundFinishedState(newState)
            }
        }
    }

    private fun setRoundFinishedState(gameInProgressState: GameScreenUiState.RoundInProgress) {
        gameScreenUiState = GameScreenUiState.RoundFinished(
            player1Name = gameInProgressState.player1Name,
            player2Name = gameInProgressState.player2Name,
            currentLetter = gameInProgressState.currentLetter,
            missedCountries = gameInProgressState.countriesRemaining,
            numOfCountriesLeft = gameInProgressState.numOfCountriesLeft,
            player1Score = gameInProgressState.player1Score,
            player2Score = gameInProgressState.player2Score,
            isPlayer1Turn = gameInProgressState.isPlayer1Turn,
            remainingLetters = gameInProgressState.remainingLetters,
        )
    }

    fun loadNextLetter() {
        gameScreenUiState = gameInProgressState
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

        val state = gameScreenUiState as GameScreenUiState.RoundInProgress

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
        val state = gameScreenUiState as GameScreenUiState.RoundInProgress
        val missedCountries = state.countriesRemaining

        if (state.remainingLetters.isEmpty()) {
            gameScreenUiState = GameScreenUiState.GameOver
        } else {
            val currentLetter = state.remainingLetters.random()
            val countriesRemaining = getCountriesUseCase.invoke(currentLetter)
            val numOfCountriesLeft = countriesRemaining.size
            val player1Score = if (state.isPlayer1Turn) state.player1Score else state.player1Score + 1
            val player2Score = if (state.isPlayer1Turn) state.player2Score + 1 else state.player2Score
            val remainingLetters = state.remainingLetters.filter { it != currentLetter }
            val isPlayer1Turn = !state.isPlayer1Turn

            gameScreenUiState = GameScreenUiState.RoundFinished(
                player1Name = state.player1Name,
                player2Name = state.player2Name,
                player1Score = player1Score,
                player2Score = player2Score,
                currentLetter = currentLetter,
                missedCountries = missedCountries,
                numOfCountriesLeft = numOfCountriesLeft,
                remainingLetters = remainingLetters,
                isPlayer1Turn = isPlayer1Turn,
                result = if (isPlayer1Turn) "${state.player2Name} won that round!" else "${state.player1Name} won that round!",
                resultBackgroundColor = Color.Green,
            )

            gameInProgressState = state.copy(
                currentLetter = currentLetter,
                countriesRemaining = countriesRemaining,
                numOfCountriesLeft = numOfCountriesLeft,
                player1Countries = emptyList(),
                player2Countries = emptyList(),
                remainingLetters = remainingLetters,
                showCountriesRemaining = true,
                player1Score = player1Score,
                player2Score = player2Score,
                isPlayer1Turn = isPlayer1Turn,
                player1TurnColor = getTurnColor(isPlayer1Turn = !state.isPlayer1Turn, player = Players.Player1),
                player2TurnColor = getTurnColor(isPlayer1Turn = !state.isPlayer1Turn, player = Players.Player2),
            )
        }
    }
    fun hideBottomSheet() {
        bottomSheetState = BottomSheetState.Hide
    }

    fun showBottomSheet(country: Country) {
        viewModelScope.launch {

            val description = getCountriesUseCase.getDescription(country.name)

            bottomSheetState = BottomSheetState.Show(
                countryName = country.name,
                imgUrl = country.imgUrl,
                description = description
            )
        }
    }
    fun showBottomSheet(country: String) {
        viewModelScope.launch {
            val description = getCountriesUseCase.getDescription(country)

            bottomSheetState = BottomSheetState.Show(
                countryName = country,
                imgUrl = "",
                description = description
            )
        }
    }
}