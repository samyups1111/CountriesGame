package com.example.countriesgame.server

import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.ui.gamescreen.state.CountryGameState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GameStateManager @Inject constructor() {

    var countryGameState : MutableStateFlow<CountryGameState> = MutableStateFlow(CountryGameState.Loading)
        private set

    var prevState = CountryGameState.RoundInProgress()

    private var gameInProgressState = CountryGameState.RoundInProgress()

    fun setStartState(
        startingLetter: Char,
        countriesRemaining: List<Country>,
        remainingLetters: List<Char>,
    ) {
        val startState = CountryGameState.RoundInProgress(
            currentLetter = startingLetter,
            countriesRemaining = countriesRemaining,
            numOfCountriesLeft = countriesRemaining.size,
            player1TurnColor = Color.Yellow,
            remainingLetters = remainingLetters,
        )
        countryGameState.value = startState
    }

    fun updateSearchBarState(text: String) {
        countryGameState.value = prevState.copy(searchBarText = text)

    }

    fun savePreviousRoundInProgressState() {
        prevState = countryGameState.value as CountryGameState.RoundInProgress
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
    ) {
        if (prevState.remainingLetters.isEmpty()) {
            countryGameState.value = CountryGameState.CountryGameOver
            return
        }

        val numOfCountriesRemaining = countriesRemainingThisRound.size
        val remainingLetters = prevState.remainingLetters.filter { it != currentLetter }
        val isPlayer1Turn = !prevState.isPlayer1Turn
        val result = if (prevState.isPlayer1Turn) "${prevState.player2Name} won that round!" else "${prevState.player1Name} won that round!"

        countryGameState.value = CountryGameState.RoundFinished(
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
            player1TurnColor = player1TurnColor,
            player2TurnColor = player2TurnColor,
        )
    }
    private fun saveInProgressState(
        prevState: CountryGameState.RoundInProgress,
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
        countryGameState.value = prevState.copy(
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
        countryGameState.value = gameInProgressState
    }
}