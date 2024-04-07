package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.User
import com.example.countriesgame.ui.gamescreen.viewdata.GameStateLabel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class GameServerImpl @Inject constructor(): GameServer {

    override var gameState : MutableStateFlow<GameState> = MutableStateFlow(GameState())
        private set
    private lateinit var allCountries: List<Country>

    private val allLettersWithCountryInitial = listOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'L','M', 'N', 'O', 'P', 'Q', 'R','S', 'T', 'U', 'V', 'Y', 'Z'
    )

    override fun setCountries(countries: List<Country>) { allCountries = countries }

    override fun setPlayers(
        userOne: User,
        userTwo: User,
    ) {
        gameState.update {
            it.copy(
                user1 = userOne,
                user2 = userTwo,
            )
        }
    }

    override fun startGame() {
        val startingLetter = allLettersWithCountryInitial.random()
        val countriesRemaining = getCountriesByLetter(startingLetter)
        val lettersRemaining = getAllLettersExcept(startingLetter)

        gameState.update {
            it.copy(
                gameStateLabel = GameStateLabel.GAME_IN_PROGRESS,
                currentLetter = startingLetter,
                countriesRemaining = countriesRemaining,
                numOfCountriesLeft = countriesRemaining.size,
                remainingLetters = lettersRemaining,
                currentAnswer = "",
            )
        }
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

        gameState.update {
            it.copy(
                gameStateLabel = GameStateLabel.GAME_IN_PROGRESS,
                user1 = it.user1.copy(countriesGuessedCorrectly = emptyList()),
                user2 = it.user2.copy(countriesGuessedCorrectly = emptyList()),
                currentLetter = nextLetter,
                countriesRemaining = countriesRemainingNextRound,
                numOfCountriesLeft = countriesRemainingNextRound.size,
                remainingLetters = getRemainingLetters(letterToFilter = nextLetter),
                currentAnswer = "",
            )
        }
    }

    private fun getNewRandomLetter(): Char = gameState.value.remainingLetters.random()

    private fun getRemainingLetters(letterToFilter: Char): List<Char> {
        return gameState.value.remainingLetters.filter { it != letterToFilter }
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
        gameState.update { it.copy(currentAnswer = newAnswer) }
    }

    private fun Country.hasName(answer: String): Boolean {
        val answerFormatted = answer.trim()
        return answerFormatted.equals(name.official, ignoreCase = true) || answerFormatted.inCommonNames(name.common)
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

    private fun moreCountriesRemainingThisRound(): Boolean = gameState.value.numOfCountriesLeft - 1 > 0

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

        if (isGameOver()) setGameOverState()
        else {
            gameState.update {
                it.copy(
                    gameStateLabel = GameStateLabel.BETWEEN_ROUNDS,
                    user1 = state.user1.copy(isRoundWinner = false),
                    user2 = state.user2.copy(isRoundWinner = false),
                    currentLetter = state.currentLetter,
                    countriesRemaining = emptyList(),
                    remainingLetters = state.remainingLetters,
                )
            }
        }
    }

    private fun isGameOver(): Boolean {
        val remainingLetters = gameState.value.remainingLetters
        return remainingLetters.isEmpty()
    }

    private fun setGameOverState() {
        val state = gameState.value
        val player1Score = state.user1.score
        val player2Score = state.user2.score
        val winner = if (player1Score < player2Score) state.user2 else state.user1
        gameState.update {
            it.copy(
                gameStateLabel = GameStateLabel.GAME_OVER,
                gameWinner = winner,
            )
        }
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

        if (isGameOver()) setGameOverState()
        else
        gameState.update {
            it.copy(
                gameStateLabel = GameStateLabel.BETWEEN_ROUNDS,
                user1 = player1,
                user2 = player2,
                currentLetter = prevState.currentLetter,
                countriesRemaining = prevState.countriesRemaining,
                numOfCountriesLeft = prevState.countriesRemaining.size,
                remainingLetters = prevState.remainingLetters,
                //roundWinner = roundWinner,
            )
        }
    }

    private fun getRoundWinnerAfterGiveUp(): User {
        val state = gameState.value
        val isCurrentlyPlayer1Turn = state.user1.isItsTurn
        return if (isCurrentlyPlayer1Turn) state.user2 else state.user1
    }

    private fun getUpdatedScore(user: User): Int {
        return if (user.isItsTurn) user.score else user.score + 1
    }
}