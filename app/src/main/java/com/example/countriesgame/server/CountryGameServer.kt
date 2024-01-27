package com.example.countriesgame.server

import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.ui.gamescreen.state.CountryGameState
import com.example.countriesgame.model.Players
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CountryGameServer @Inject constructor() {

    private var allCountries: List<Country> = emptyList()
    var countryGameState : MutableStateFlow<CountryGameState> = MutableStateFlow(CountryGameState.Loading)
        private set

    private var gameInProgressState = CountryGameState.RoundInProgress()

    fun loadCountries(countries: List<Country>) {
        allCountries = countries
    }

    fun startGame() {
        val currentLetter = CountryGameState.qualifiedLetters.random()
        val countriesRemaining = getCountriesByRandomLetter(currentLetter)

        val startState = CountryGameState.RoundInProgress(
            currentLetter = currentLetter,
            countriesRemaining = countriesRemaining,
            numOfCountriesLeft = countriesRemaining.size,
            player1TurnColor = Color.Green,
        )
        countryGameState.value = startState
    }

    fun onPlayerAnswered(countryGuessed: String) {

        val prevState = countryGameState.value as CountryGameState.RoundInProgress
        updateSearchBar(countryGuessed, prevState)

        prevState.countriesRemaining.forEach { country ->
            if (isCountryCorrectGuess(country, countryGuessed)) {
                onGuessedCorrectly(country.name.common, prevState)
            }
        }
    }

    private fun isCountryCorrectGuess(country: Country, guess: String): Boolean {

        val guessFormatted = guess.trim()
        val commonNameList = country.name.common.split(',')

        return country.name.official.equals(guessFormatted, ignoreCase = true) ||
                commonNameList.any {  name -> name.equals(guessFormatted, ignoreCase = true) }
    }

    private fun onGuessedCorrectly(countryName: String, prevState: CountryGameState.RoundInProgress) {

        val numOfCountriesRemaining = prevState.numOfCountriesLeft - 1

        if (numOfCountriesRemaining < 1) {
            setRoundFinishedState(prevState) // todo: bug: missing countries showed last remaining country when guess correctly
        } else {
            countryGameState.value = prevState.copy(
                countriesRemaining = prevState.countriesRemaining.filter { it.name.common != countryName && it.name.official != countryName},
                player1Countries = addCountryToCorrectlyGuessedList(country = countryName,
                    Players.Player1
                ),
                player2Countries = addCountryToCorrectlyGuessedList(country = countryName,
                    Players.Player2
                ),
                searchBarText = "",
                numOfCountriesLeft = numOfCountriesRemaining,
                isPlayer1Turn = !prevState.isPlayer1Turn,
                player1TurnColor = getScoreBoardColor(
                    isPlayer1Turn = !prevState.isPlayer1Turn,
                    player = Players.Player1,
                ),
                player2TurnColor = getScoreBoardColor(
                    isPlayer1Turn = !prevState.isPlayer1Turn,
                    player = Players.Player2,
                ),
            )
        }
    }

    fun updateStateOnGiveUp() {
        val prevState = countryGameState.value as CountryGameState.RoundInProgress

        setRoundFinishedState(prevState, awardPoint = true)
    }

    private fun setRoundFinishedState(prevState: CountryGameState.RoundInProgress, awardPoint: Boolean = false) {

        if (prevState.remainingLetters.isEmpty()) {
            countryGameState.value = CountryGameState.CountryGameOver
            return
        }

        val player1Score = if (awardPoint && !prevState.isPlayer1Turn) prevState.player1Score + 1 else prevState.player1Score
        val player2Score = if (awardPoint && prevState.isPlayer1Turn) prevState.player2Score + 1 else prevState.player2Score
        val currentLetter = prevState.remainingLetters.random()
        val missedCountriesPrevRound = if (awardPoint) prevState.countriesRemaining else emptyList()
        val countriesRemainingThisRound = allCountries.filter { it.name.common.first() == currentLetter }
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
            result = if (awardPoint) result else "Draw",
            resultBackgroundColor = if (awardPoint) Color.Green else Color.LightGray,
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
            player1TurnColor = getScoreBoardColor(
                isPlayer1Turn = isPlayer1Turn,
                player = Players.Player1,
            ),
            player2TurnColor = getScoreBoardColor(
                isPlayer1Turn = isPlayer1Turn,
                player = Players.Player2,
            ),
            remainingLetters = remainingLetters,
        )
    }

    private fun addCountryToCorrectlyGuessedList(country: String, player: Players): List<String> {

        val state = countryGameState.value as CountryGameState.RoundInProgress

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

    fun startNextRound() {
        countryGameState.value = gameInProgressState
    }

    private fun getScoreBoardColor(isPlayer1Turn: Boolean, player: Players): Color {
        return when (player) {
            is Players.Player1 -> {
                if (isPlayer1Turn) Color.Green else Color.LightGray
            }
            is Players.Player2 -> {
                if (isPlayer1Turn) Color.LightGray else Color.Green
            }
        }
    }

    private fun updateSearchBar(countryName: String, state: CountryGameState.RoundInProgress) {
        countryGameState.value = state.copy(searchBarText = countryName)
    }

    private fun getCountriesByRandomLetter(letter: Char): List<Country> {
        return allCountries.filter { it.name.common.first() == letter }
    }
}