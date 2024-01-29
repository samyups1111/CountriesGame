package com.example.countriesgame.server

import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.ui.gamescreen.state.GameState
import com.example.countriesgame.model.Players
import com.example.countriesgame.model.RoundResult
import javax.inject.Inject

class CountryGameServer @Inject constructor(
    private val gameStateManager: GameStateManager,
) {

    private var allCountries: List<Country> = emptyList()
    var countryGameState = gameStateManager.gameState

    fun loadCountries(countries: List<Country>) {
        allCountries = countries
    }

    fun startGame() {
        val currentLetter = GameState.qualifiedLetters.random()
        val countriesRemaining = getCountriesByLetter(currentLetter)
        val remainingLetters = getLettersRemaining(currentLetter, GameState.qualifiedLetters)

        gameStateManager.setStartState(
            startingLetter = currentLetter,
            countriesRemaining = countriesRemaining,
            remainingLetters = remainingLetters,
        )
    }

    private fun getLettersRemaining(currentLetter: Char, letters: List<Char>): List<Char> {
        return letters.filter { it != currentLetter }
    }

    fun onAnswerSubmitted(countryGuessed: String) {

        gameStateManager.savePreviousRoundInProgressState()
        gameStateManager.updateSearchBarState(countryGuessed)

        gameStateManager.prevState.countriesRemaining.forEach { country ->
            if (isAnswerCorrect(country, countryGuessed)) {
                onCorrectAnswerSubmitted(country.name.common)
            }
        }
    }

    private fun isAnswerCorrect(country: Country, guess: String): Boolean {

        val guessFormatted = guess.trim()
        val commonNameList = country.name.common.split(',')

        return country.name.official.equals(guessFormatted, ignoreCase = true) ||
                commonNameList.any {  name -> name.equals(guessFormatted, ignoreCase = true) }
    }

    private fun onCorrectAnswerSubmitted(countryName: String) {

        val numOfCountriesRemaining = gameStateManager.prevState.numOfCountriesLeft - 1

        if (numOfCountriesRemaining < 1) {
            setRoundFinishedState() // todo: bug: missing countries showed last remaining country when guess correctly
        } else {
            val countriesRemaining = gameStateManager.prevState.countriesRemaining.filter { it.name.common != countryName && it.name.official != countryName}
            val player1Countries = addCountryToCorrectlyGuessedList(
                country = countryName,
                player = Players.Player1,
            )
            val player2Countries = addCountryToCorrectlyGuessedList(
                country = countryName,
                Players.Player2,
            )
            val player1TurnColor = getScoreBoardColor(
                isPlayer1Turn = !gameStateManager.prevState.isPlayer1Turn,
                player = Players.Player1,
            )
            val player2TurnColor = getScoreBoardColor(
                isPlayer1Turn = !gameStateManager.prevState.isPlayer1Turn,
                player = Players.Player2,
            )
            gameStateManager.continueRoundState(
                countriesRemaining = countriesRemaining,
                player1Countries = player1Countries,
                player2Countries = player2Countries,
                numOfCountriesRemaining = numOfCountriesRemaining,
                player1TurnColor = player1TurnColor,
                player2TurnColor = player2TurnColor,
            )
        }
    }

    fun updateStateOnGiveUp() {
        gameStateManager.savePreviousRoundInProgressState()

        val roundResult = if (gameStateManager.prevState.isPlayer1Turn) RoundResult.Player2Won else RoundResult.Player1Won

        setRoundFinishedState(roundResult = roundResult)
    }

    private fun setRoundFinishedState(roundResult: RoundResult = RoundResult.Draw) {

        val player1Score = getPlayer1Score(
            roundResult = roundResult,
            player1Score = gameStateManager.prevState.player1Score,
        )
        val player2Score = getPlayer2Score(
            roundResult = roundResult,
            player2Score = gameStateManager.prevState.player2Score,
        )
        val missedCountriesPrevRound = if (roundResult == RoundResult.Draw) emptyList() else gameStateManager.prevState.countriesRemaining
        val currentLetter = gameStateManager.prevState.remainingLetters.random()
        val countriesRemainingThisRound = allCountries.filter { it.name.common.first() == currentLetter }
        val resultBackgroundColor = if (roundResult == RoundResult.Draw) Color.LightGray else Color.Yellow

        val player1TurnColor = getScoreBoardColor(
            isPlayer1Turn = gameStateManager.prevState.isPlayer1Turn,
            player = Players.Player1,
        )
        val player2TurnColor = getScoreBoardColor(
            isPlayer1Turn = gameStateManager.prevState.isPlayer1Turn,
            player = Players.Player2,
        )
        val remainingLetters = gameStateManager.prevState.remainingLetters.filter { it != currentLetter }
        val isPlayer1Turn = !gameStateManager.prevState.isPlayer1Turn
        val result = if (isPlayer1Turn) "${gameStateManager.prevState.player2Name} won that round!" else "${gameStateManager.prevState.player1Name} won that round!"

        gameStateManager.setRoundFinishedState(
            player1Score = player1Score,
            player2Score = player2Score,
            missedCountriesPrevRound = missedCountriesPrevRound,
            currentLetter = currentLetter,
            countriesRemainingThisRound = countriesRemainingThisRound,
            resultBackgroundColor = resultBackgroundColor,
            player1TurnColor = player1TurnColor,
            player2TurnColor = player2TurnColor,
            remainingLetters = remainingLetters,
            isPlayer1Turn = isPlayer1Turn,
            result = result,
        )
    }

    private fun getPlayer1Score(
        roundResult: RoundResult,
        player1Score: Int,
    ): Int {
        return if (roundResult is RoundResult.Player1Won) player1Score + 1 else player1Score
    }

    private fun getPlayer2Score(
        roundResult: RoundResult,
        player2Score: Int,
    ): Int {
        return if (roundResult is RoundResult.Player2Won) player2Score + 1 else player2Score
    }

    private fun addCountryToCorrectlyGuessedList(country: String, player: Players): List<String> {

        val state = gameStateManager.prevState

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

    fun startNextRound() = gameStateManager.startNextRound()

    private fun getScoreBoardColor(isPlayer1Turn: Boolean, player: Players): Color {
        return when (player) {
            is Players.Player1 -> {
                if (isPlayer1Turn) Color.Yellow else Color.LightGray
            }
            is Players.Player2 -> {
                if (isPlayer1Turn) Color.LightGray else Color.Yellow
            }
        }
    }

    private fun getCountriesByLetter(letter: Char): List<Country> {
        return allCountries.filter { it.name.common.first() == letter }
    }
}