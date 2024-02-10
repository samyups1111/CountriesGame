package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GameServerImp @Inject constructor(): GameServer {
    override var allCountries: List<Country> = emptyList()
    override var gameState : MutableStateFlow<GameState> = MutableStateFlow(GameState.Loading)
        private set

    override fun setCountries(countries: List<Country>) {
        allCountries = countries
    }

    override fun startGame(
        player1: Player,
        player2: Player,
    ) {
        val startingLetter = allLettersWithCountryInitial.random()
        val countriesRemaining = getCountriesByLetter(startingLetter)
        val lettersRemaining = getAllLettersExcept(startingLetter)
        val startState = GameState.RoundInProgress(
            player1 = player1,
            player2 = player2,
            currentLetter = startingLetter,
            countriesRemaining = countriesRemaining,
            numOfCountriesLeft = countriesRemaining.size,
            remainingLetters = lettersRemaining,
            currentAnswer = "",
        )
        gameState.value = startState
    }

    private fun getCountriesByLetter(letter: Char): List<Country> {
        return allCountries.filter { it.name.common.first() == letter }
    }

    private fun getAllLettersExcept(currentLetter: Char): List<Char> {
        return allLettersWithCountryInitial.filter { it != currentLetter }
    }

    override fun startNextRound() {
        val currentState = gameState.value as GameState.RoundFinished
        val currentLetter = getNewRandomLetter(currentState.remainingLetters)
        val countriesRemainingThisRound = getCountriesByLetter(currentLetter)
        val remainingLetters = getRemainingLettersAfterFilteringOut(currentState.remainingLetters, currentLetter)
        val player1 = currentState.player1.copy(
            countriesGuessedCorrectly = emptyList(),
        )
        val player2 = currentState.player2.copy(
            countriesGuessedCorrectly = emptyList(),
        )

        gameState.value = GameState.RoundInProgress(
            player1 = player1,
            player2 = player2,
            currentLetter = currentLetter,
            countriesRemaining = countriesRemainingThisRound,
            numOfCountriesLeft = countriesRemainingThisRound.size,
            remainingLetters = remainingLetters,
            currentAnswer = "",
        )
    }

    override fun onAnswerSubmitted(answer: String) {
        updateAnswer(answer)
        (gameState.value as GameState.RoundInProgress).countriesRemaining.forEach { country ->
            if (answerMatchesCountry(answer, country)) {
                onCorrectAnswerSubmitted(country.name.common)
            }
        }
    }

    private fun updateAnswer(newAnswer: String) {
        gameState.value = (gameState.value as GameState.RoundInProgress).copy(currentAnswer = newAnswer)
    }

    private fun answerMatchesCountry(answer: String, country: Country): Boolean {
        val guessFormatted = answer.trim()
        val commonNameList = country.name.common.split(',')
        return country.name.official.equals(guessFormatted, ignoreCase = true) ||
                commonNameList.any {  name -> name.equals(guessFormatted, ignoreCase = true) }
    }

    private fun onCorrectAnswerSubmitted(countryName: String) {
        if (moreCountriesStillRemainThisRound()) updateRoundInProgressAfterCorrectAnswer(countryName)
        else roundFinishedInDraw()
    }

    private fun moreCountriesStillRemainThisRound(): Boolean {
        val countriesRemainingThisRound = (gameState.value as GameState.RoundInProgress).numOfCountriesLeft - 1
        return countriesRemainingThisRound > 0
    }

    private fun updateRoundInProgressAfterCorrectAnswer(answer: String) {
        val answerCountry = getCountryByName(answer)
        val currentState = gameState.value as GameState.RoundInProgress
        val numOfCountriesRemaining = currentState.numOfCountriesLeft - 1
        val countriesRemaining = currentState.countriesRemaining.filter { it.name.common != answer && it.name.official != answer}
        val player1Countries = getUpdatedCorrectlyAnsweredCountries(
            country = answerCountry,
            player = currentState.player1,
        )
        val player2Countries = getUpdatedCorrectlyAnsweredCountries(
            country = answerCountry,
            player = currentState.player2,
        )
        val player1 = currentState.player1.copy(
            countriesGuessedCorrectly = player1Countries,
            isItsTurn = !currentState.player1.isItsTurn,
        )
        val player2 = currentState.player2.copy(
            countriesGuessedCorrectly = player2Countries,
            isItsTurn = !currentState.player2.isItsTurn,
        )
        gameState.value = currentState.copy(
            countriesRemaining = countriesRemaining,
            player1 = player1,
            player2 = player2,
            numOfCountriesLeft = numOfCountriesRemaining,
            currentAnswer = "",
        )
    }

    private fun getCountryByName(name: String): Country {
        return allCountries.first { it.name.common == name }
    }

    private fun getUpdatedCorrectlyAnsweredCountries(country: Country, player: Player): List<Country> {
        return if (player.isItsTurn) addCountryToList(country, player.countriesGuessedCorrectly)
        else player.countriesGuessedCorrectly
    }

    private fun addCountryToList(country: Country, countries: List<Country>): List<Country> {
        val countriesMutable = countries.toMutableList()
        countriesMutable.add(country)
        return countriesMutable.toList()
    }

    private fun roundFinishedInDraw() {
        val state = (gameState.value as GameState.RoundInProgress)

        if (isGameOver()) return

        gameState.value = GameState.RoundFinished(
            player1 = state.player1,
            player2 = state.player2,
            currentLetter = state.currentLetter,
            missedCountries = emptyList(),
            remainingLetters = state.remainingLetters,
        )
    }

    private fun getNewRandomLetter(remainingLetters: List<Char>): Char {
        return remainingLetters.random()
    }

    private fun getRemainingLettersAfterFilteringOut(remainingLetters: List<Char>, currentLetter: Char): List<Char> {
        return remainingLetters.filter { it != currentLetter }
    }

    private fun isGameOver(): Boolean {
        val remainingLetters = (gameState.value as GameState.RoundInProgress).remainingLetters
        return if (remainingLetters.isEmpty()) {
            setGameOverState()
            true
        } else false
    }

    private fun setGameOverState() {
        val state = gameState.value as GameState.RoundInProgress
        val player1Score = state.player1.score
        val player2Score = state.player2.score
        val winner = if (player1Score < player2Score) state.player1
        else state.player1
        gameState.value = GameState.GameOver(
            winner = winner,
        )
    }

    override fun onGiveUp() {
        val prevState = gameState.value as GameState.RoundInProgress
        val player1Score = getUpdatedScore(prevState.player1)
        val player2Score = getUpdatedScore(prevState.player2)
        val player1 = prevState.player1.copy(
            score = player1Score,
            isItsTurn = !prevState.player1.isItsTurn,
        )
        val player2 = prevState.player2.copy(
            score = player2Score,
            isItsTurn = !prevState.player2.isItsTurn,
        )

        if (isGameOver()) return

        gameState.value = GameState.RoundFinished(
            player1 = player1,
            player2 = player2,
            currentLetter = prevState.currentLetter,
            missedCountries = prevState.countriesRemaining,
            numOfMissedCountries = prevState.countriesRemaining.size,
            remainingLetters = prevState.remainingLetters,
        )
    }

    private fun getUpdatedScore(player: Player): Int {
        return if (!player.isItsTurn) player.score + 1 else player.score
    }

    companion object {
        val allLettersWithCountryInitial = listOf(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L','M', 'N', 'O', 'P', 'Q', 'R','S', 'T', 'U', 'V', 'Y', 'Z'
        )
    }
}