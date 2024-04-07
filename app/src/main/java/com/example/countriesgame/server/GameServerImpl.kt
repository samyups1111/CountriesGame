package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.User
import com.example.countriesgame.model.gamescreen.GameStateLabel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GameServerImpl @Inject constructor(): GameServer {

    override var gameState : MutableStateFlow<GameState> = MutableStateFlow(GameState())
        private set
    private lateinit var allCountries: List<Country>
    private lateinit var user1: User
    private lateinit var user2: User

    override fun setCountries(countries: List<Country>) {
        allCountries = countries
    }

    override fun setPlayers(
        userOne: User,
        userTwo: User,
    ) {
        user1 = userOne
        user2 = userTwo
    }

    override fun startGame() {
        val startingLetter = allLettersWithCountryInitial.random()
        val countriesRemaining = getCountriesByLetter(startingLetter)
        val lettersRemaining = getAllLettersExcept(startingLetter)
        val startState = GameState(
            gameStateLabel = GameStateLabel.GAME_IN_PROGRESS,
            user1 = user1,
            user2 = user2,
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
        val nextLetter = getNewRandomLetter()
        val countriesRemainingNextRound = getCountriesByLetter(nextLetter)
        val remainingLettersNextRound = getRemainingLetters(letterToFilter = nextLetter)
        val currentState = gameState.value
        val player1 = currentState.user1.copy(countriesGuessedCorrectly = emptyList())
        val player2 = currentState.user2.copy(countriesGuessedCorrectly = emptyList())

        gameState.value = GameState(
            gameStateLabel = GameStateLabel.GAME_IN_PROGRESS,
            user1 = player1,
            user2 = player2,
            currentLetter = nextLetter,
            countriesRemaining = countriesRemainingNextRound,
            numOfCountriesLeft = countriesRemainingNextRound.size,
            remainingLetters = remainingLettersNextRound,
            currentAnswer = "",
        )
    }

    private fun getNewRandomLetter(): Char {
        val remainingLetters = gameState.value.remainingLetters
        return remainingLetters.random()
    }

    private fun getRemainingLetters(letterToFilter: Char): List<Char> {
        val remainingLetters = gameState.value.remainingLetters
        return remainingLetters.filter { it != letterToFilter }
    }

    override fun onAnswerSubmitted(answer: String) {
        val countriesRemaining = gameState.value.countriesRemaining
        updateAnswer(answer)
        countriesRemaining.forEach { country ->
            if (country.hasName(answer)) {
                onCorrectAnswerSubmitted(country.name.common)
            }
        }
    }

    private fun updateAnswer(newAnswer: String) {
        gameState.value = gameState.value.copy(currentAnswer = newAnswer)
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
        val prevNumOfCountriesRemaining = gameState.value.numOfCountriesLeft
        val currentNumOfCountriesRemaining = prevNumOfCountriesRemaining - 1
        return currentNumOfCountriesRemaining > 0
    }

    private fun updateRoundInProgressAfterCorrectAnswer(answer: String) {
        val answerCountry = getCountryByName(answer)
        val currentState = gameState.value
        val numOfCountriesRemaining = currentState.numOfCountriesLeft - 1
        val countriesRemaining = getRemainingCountries(answer)
        val player1Countries = getUpdatedCorrectlyAnsweredCountries(
            country = answerCountry,
            user = currentState.user1,
        )
        val player2Countries = getUpdatedCorrectlyAnsweredCountries(
            country = answerCountry,
            user = currentState.user2,
        )
        val player1 = currentState.user1.copy(
            countriesGuessedCorrectly = player1Countries,
            isItsTurn = !currentState.user1.isItsTurn,
        )
        val player2 = currentState.user2.copy(
            countriesGuessedCorrectly = player2Countries,
            isItsTurn = !currentState.user2.isItsTurn,
        )
        gameState.value = currentState.copy(
            countriesRemaining = countriesRemaining,
            user1 = player1,
            user2 = player2,
            numOfCountriesLeft = numOfCountriesRemaining,
            currentAnswer = "",
        )
    }

    private fun getCountryByName(name: String): Country {
        return allCountries.first { it.name.common == name }
    }

    private fun getRemainingCountries(answer: String): List<Country> {
        val countriesRemaining = gameState.value.countriesRemaining
        return countriesRemaining.filter { it.name.common != answer && it.name.official != answer}
    }

    private fun getUpdatedCorrectlyAnsweredCountries(country: Country, user: User): List<Country> {
        return if (user.isItsTurn) addCountryToList(country, user.countriesGuessedCorrectly)
        else user.countriesGuessedCorrectly
    }

    private fun addCountryToList(country: Country, countries: List<Country>): List<Country> {
        val countriesMutable = countries.toMutableList()
        countriesMutable.add(country)
        return countriesMutable.toList()
    }

    private fun roundFinishedInDraw() {
        val state = gameState.value

        if (isGameOver()) return

        gameState.value = GameState(
            gameStateLabel = GameStateLabel.BETWEEN_ROUNDS,
            user1 = state.user1.copy(isRoundWinner = false),
            user2 = state.user2.copy(isRoundWinner = false),
            currentLetter = state.currentLetter,
            countriesRemaining = emptyList(),
            remainingLetters = state.remainingLetters,
        )
    }

    private fun isGameOver(): Boolean {
        val remainingLetters = gameState.value.remainingLetters
        return if (remainingLetters.isEmpty()) {
            setGameOverState()
            true
        } else false
    }

    private fun setGameOverState() {
        val state = gameState.value
        val player1Score = state.user1.score
        val player2Score = state.user2.score
        val winner = if (player1Score < player2Score) state.user2 else state.user1
        //gameState.value = GameState(winner = winner)
    }

    override fun onGiveUp() {
        val prevState = gameState.value
        val roundWinner = getRoundWinnerAfterGiveUp()
        val player1Score = getUpdatedScore(prevState.user1)
        val player2Score = getUpdatedScore(prevState.user2)
        val player1 = prevState.user1.copy(
            score = player1Score,
            isItsTurn = !prevState.user1.isItsTurn,
        )
        val player2 = prevState.user2.copy(
            score = player2Score,
            isItsTurn = !prevState.user2.isItsTurn,
        )

        if (isGameOver()) return

        gameState.value = GameState(
            gameStateLabel = GameStateLabel.BETWEEN_ROUNDS,
            user1 = player1,
            user2 = player2,
            currentLetter = prevState.currentLetter,
            //missedCountries = prevState.countriesRemaining,
            //numOfMissedCountries = prevState.countriesRemaining.size,
            remainingLetters = prevState.remainingLetters,
            //roundWinner = roundWinner,
        )
    }

    private fun getRoundWinnerAfterGiveUp(): User {
        val state = gameState.value
        val isCurrentlyPlayer1Turn = state.user1.isItsTurn
        return if (isCurrentlyPlayer1Turn) state.user2 else state.user1
    }

    private fun getUpdatedScore(user: User): Int {
        return if (user.isItsTurn) user.score else user.score + 1
    }

    companion object {
        val allLettersWithCountryInitial = listOf(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L','M', 'N', 'O', 'P', 'Q', 'R','S', 'T', 'U', 'V', 'Y', 'Z'
        )
    }
}