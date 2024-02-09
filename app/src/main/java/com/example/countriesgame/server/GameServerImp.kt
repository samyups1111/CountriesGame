package com.example.countriesgame.server

import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.Players
import com.example.countriesgame.model.RoundResult
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class GameServerImp @Inject constructor(
    private val gameStateManager: GameStateManager,
): GameServer {
    override var allCountries: List<Country> = emptyList()
    override val gameState = gameStateManager.gameState.asStateFlow()

    override fun setCountries(countries: List<Country>) {
        allCountries = countries
    }

    override fun startGame() {
        val startingLetter = qualifiedLetters.random()
        val countriesRemaining = getCountriesByLetter(startingLetter)
        val lettersRemaining = getAllLettersExcept(startingLetter)
        gameStateManager.setStartState(
            startingLetter = startingLetter,
            countriesRemaining = countriesRemaining,
            remainingLetters = lettersRemaining,
        )
    }

    private fun getAllLettersExcept(currentLetter: Char): List<Char> {
        return qualifiedLetters.filter { it != currentLetter }
    }

    override fun startNextRound() = gameStateManager.startNextRound()

    override fun onAnswerSubmitted(answer: String) {
        gameStateManager.savePreviousRoundInProgressState()
        gameStateManager.updateSearchBarState(answer)
        gameStateManager.prevState.countriesRemaining.forEach { country ->
            if (answerMatchesCountry(answer, country)) {
                onCorrectAnswerSubmitted(country.name.common)
            }
        }
    }

    private fun answerMatchesCountry(answer: String, country: Country): Boolean {
        val guessFormatted = answer.trim()
        val commonNameList = country.name.common.split(',')
        return country.name.official.equals(guessFormatted, ignoreCase = true) ||
                commonNameList.any {  name -> name.equals(guessFormatted, ignoreCase = true) }
    }

    private fun onCorrectAnswerSubmitted(countryName: String) {
        if (moreCountriesStillRemainThisRound()) updateContinueRoundState(countryName)
        else setRoundFinishedState()
    }

    private fun moreCountriesStillRemainThisRound(): Boolean {
        val countriesRemainingThisRound = gameStateManager.prevState.numOfCountriesLeft - 1
        return countriesRemainingThisRound > 0
    }

    private fun updateContinueRoundState(countryName: String) {
        val numOfCountriesRemaining = gameStateManager.prevState.numOfCountriesLeft - 1
        val countriesRemaining = gameStateManager.prevState.countriesRemaining.filter { it.name.common != countryName && it.name.official != countryName}
        val player1Countries = addCountryToPlayersCorrectlyGuessedList(
            country = countryName,
            player = Players.Player1,
        )
        val player2Countries = addCountryToPlayersCorrectlyGuessedList(
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

    private fun setRoundFinishedState(roundResult: RoundResult = RoundResult.Draw) {
        val player1Score = getPlayer1Score(
            roundResult = roundResult,
            player1Score = gameStateManager.prevState.player1Score,
        )
        val player2Score = getPlayer2Score(
            roundResult = roundResult,
            player2Score = gameStateManager.prevState.player2Score,
        )
        val missedCountriesPrevRound = getMissedCountriesPrevRound(roundResult)
        val currentLetter = getNewRandomLetter()
        val countriesRemainingThisRound = getCountriesByLetter(currentLetter)
        val resultBackgroundColor = getColorForResultBackground(roundResult)
        val player1TurnColor = getScoreBoardColor(
            isPlayer1Turn = gameStateManager.prevState.isPlayer1Turn,
            player = Players.Player1,
        )
        val player2TurnColor = getScoreBoardColor(
            isPlayer1Turn = gameStateManager.prevState.isPlayer1Turn,
            player = Players.Player2,
        )
        val remainingLetters = getRemainingLettersAfterFilteringOut(currentLetter)
        val isPlayer1Turn = !gameStateManager.prevState.isPlayer1Turn
        val result = getRoundResultText(isPlayer1Turn)
        val leadingPlayer = getCurrentlyWinningPlayer(player1Score, player2Score)

        if (isGameOver(remainingLetters, leadingPlayer)) return

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

    private fun getMissedCountriesPrevRound(result: RoundResult): List<Country> {
        return if (result == RoundResult.Draw) emptyList() else gameStateManager.prevState.countriesRemaining
    }

    private fun getNewRandomLetter(): Char {
        return gameStateManager.prevState.remainingLetters.random()
    }

    private fun getColorForResultBackground(result: RoundResult): Color {
        return if (result == RoundResult.Draw) Color.LightGray else Color.Yellow
    }

    private fun getRemainingLettersAfterFilteringOut(currentLetter: Char): List<Char> {
        return gameStateManager.prevState.remainingLetters.filter { it != currentLetter }
    }

    private fun getRoundResultText(isPlayer1Turn: Boolean): String {
        return if (isPlayer1Turn) "${gameStateManager.prevState.player2Name} won that round!"
        else "${gameStateManager.prevState.player1Name} won that round!"
    }

    private fun getCountriesByLetter(letter: Char): List<Country> {
        return allCountries.filter { it.name.common.first() == letter }
    }

    override fun onGiveUp() {
        gameStateManager.savePreviousRoundInProgressState()
        val roundResult = if (gameStateManager.prevState.isPlayer1Turn) RoundResult.Player2Won else RoundResult.Player1Won
        setRoundFinishedState(roundResult = roundResult)
    }

    private fun getCurrentlyWinningPlayer(
        player1Score: Int,
        player2Score: Int,
    ): String {
        return if (player1Score > player2Score) gameStateManager.prevState.player1Name
        else if (player1Score < player2Score) gameStateManager.prevState.player2Name
        else "To Both"
    }

    private fun isGameOver(
        remainingLetters: List<Char>,
        leadingPlayer: String,
    ): Boolean {
        return if (remainingLetters.isEmpty()) {
            gameStateManager.setGameOverState(leadingPlayer)
            true
        } else false
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

    private fun addCountryToPlayersCorrectlyGuessedList(country: String, player: Players): List<String> {
        val state = gameStateManager.prevState
        return when (player) {
            is Players.Player1 -> {
                getUpdatedPlayer1CorrectlyGuessedList(state, country)
            }
            is Players.Player2 -> {
                getUpdatedPlayer2CorrectlyGuessedList(state, country)
            }
        }
    }

    private fun getUpdatedPlayer1CorrectlyGuessedList(state: GameState.RoundInProgress, country: String): List<String> {
        val currentList = state.player1Countries
        return if (state.isPlayer1Turn) {
            getListWithAddedCountry(currentList, country)
        } else {
            currentList
        }
    }

    private fun getUpdatedPlayer2CorrectlyGuessedList(state: GameState.RoundInProgress, country: String): List<String> {
        val currentList = state.player2Countries
        return if (state.isPlayer1Turn) {
            currentList
        } else {
            getListWithAddedCountry(currentList, country)
        }
    }

    private fun getListWithAddedCountry(countries: List<String>, country: String): List<String> {
        val currentList = countries.toMutableStateList()
        currentList.add(country)
        return currentList.toList()
    }



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

    companion object {
        val qualifiedLetters = listOf(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L','M', 'N', 'O', 'P', 'Q', 'R','S', 'T', 'U', 'V', 'Y', 'Z'
        )
    }
}