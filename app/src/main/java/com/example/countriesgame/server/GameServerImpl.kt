package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GameServerImpl @Inject constructor(): GameServer {

    override var gameState : MutableStateFlow<GameState> = MutableStateFlow(GameState.Loading)
        private set
    private lateinit var allCountries: List<Country>
    private lateinit var player1: Player
    private lateinit var player2: Player

    override fun setCountries(countries: List<Country>) {
        allCountries = countries
    }

    override fun setPlayers(
        playerOne: Player,
        playerTwo: Player,
    ) {
        player1 = playerOne
        player2 = playerTwo
    }

    override fun startGame() {
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
        if (gameState.value !is GameState.RoundFinished) return
        val nextLetter = getNewRandomLetter()
        val countriesRemainingNextRound = getCountriesByLetter(nextLetter)
        val remainingLettersNextRound = getRemainingLetters(letterToFilter = nextLetter)
        val currentState = gameState.value as GameState.RoundFinished
        val player1 = currentState.player1.copy(countriesGuessedCorrectly = emptyList())
        val player2 = currentState.player2.copy(countriesGuessedCorrectly = emptyList())

        gameState.value = GameState.RoundInProgress(
            player1 = player1,
            player2 = player2,
            currentLetter = nextLetter,
            countriesRemaining = countriesRemainingNextRound,
            numOfCountriesLeft = countriesRemainingNextRound.size,
            remainingLetters = remainingLettersNextRound,
            currentAnswer = "",
        )
    }

    private fun getNewRandomLetter(): Char {
        val remainingLetters = (gameState.value as GameState.RoundFinished).remainingLetters
        return remainingLetters.random()
    }

    private fun getRemainingLetters(letterToFilter: Char): List<Char> {
        val remainingLetters = (gameState.value as GameState.RoundFinished).remainingLetters
        return remainingLetters.filter { it != letterToFilter }
    }

    override fun onAnswerSubmitted(answer: String) {
        val countriesRemaining = (gameState.value as GameState.RoundInProgress).countriesRemaining
        updateAnswer(answer)
        countriesRemaining.forEach { country ->
            if (country.hasName(answer)) {
                onCorrectAnswerSubmitted(country.name.common)
            }
        }
    }

    private fun updateAnswer(newAnswer: String) {
        gameState.value = (gameState.value as GameState.RoundInProgress).copy(currentAnswer = newAnswer)
    }

    private fun Country.hasName(answer: String): Boolean {
        val answerFormatted = answer.trim()
        val officialName = this.name.official
        val commonNames = this.name.common
        return answerFormatted.equals(officialName, ignoreCase = true) || answerFormatted.inCommonNames(commonNames)
    }

    private fun String.inCommonNames(commonNames: String): Boolean {
        val commonNamesList = commonNames.split(',')
        commonNamesList.forEach { name ->
            if (name.equals(this, ignoreCase = true)) return true
        }
        return false
    }

    private fun onCorrectAnswerSubmitted(countryName: String) {
        if (moreCountriesRemainingThisRound()) updateRoundInProgressAfterCorrectAnswer(countryName)
        else roundFinishedInDraw()
    }

    private fun moreCountriesRemainingThisRound(): Boolean {
        val prevNumOfCountriesRemaining = (gameState.value as GameState.RoundInProgress).numOfCountriesLeft
        val currentNumOfCountriesRemaining = prevNumOfCountriesRemaining - 1
        return currentNumOfCountriesRemaining > 0
    }

    private fun updateRoundInProgressAfterCorrectAnswer(answer: String) {
        val answerCountry = getCountryByName(answer)
        val currentState = gameState.value as GameState.RoundInProgress
        val numOfCountriesRemaining = currentState.numOfCountriesLeft - 1
        val countriesRemaining = getRemainingCountries(answer)
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

    private fun getRemainingCountries(answer: String): List<Country> {
        val countriesRemaining = (gameState.value as GameState.RoundInProgress).countriesRemaining
        return countriesRemaining.filter { it.name.common != answer && it.name.official != answer}
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
            roundWinner = Player(0, "You both", 0, emptyList(), false)
        )
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
        val winner = if (player1Score < player2Score) state.player2 else state.player1
        gameState.value = GameState.GameOver(winner = winner)
    }

    override fun onGiveUp() {
        if (gameState.value !is GameState.RoundInProgress) return
        val prevState = gameState.value as GameState.RoundInProgress
        val roundWinner = getRoundWinnerAfterGiveUp()
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
            roundWinner = roundWinner,
        )
    }

    private fun getRoundWinnerAfterGiveUp(): Player {
        val state = (gameState.value as GameState.RoundInProgress)
        val isCurrentlyPlayer1Turn = state.player1.isItsTurn
        return if (isCurrentlyPlayer1Turn) state.player2 else state.player1
    }

    private fun getUpdatedScore(player: Player): Int {
        return if (player.isItsTurn) player.score else player.score + 1
    }

    companion object {
        val allLettersWithCountryInitial = listOf(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L','M', 'N', 'O', 'P', 'Q', 'R','S', 'T', 'U', 'V', 'Y', 'Z'
        )
    }
}