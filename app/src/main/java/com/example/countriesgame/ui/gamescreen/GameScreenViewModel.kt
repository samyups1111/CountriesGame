package com.example.countriesgame.ui.gamescreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.User
import com.example.countriesgame.ui.gamescreen.viewdata.CountryBottomSheetViewData
import com.example.countriesgame.ui.gamescreen.viewdata.GamePageViewData
import com.example.countriesgame.model.usecase.GetCountriesUseCase
import com.example.countriesgame.server.GameServer
import com.example.countriesgame.server.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val gameServer: GameServer,
): ViewModel() {

    private val gameState: StateFlow<GameState> = gameServer.gameState

    private val _gamePageUiState = MutableStateFlow(GamePageViewData())
    val gamePageUiState: StateFlow<GamePageViewData> = _gamePageUiState

    private val user1 = User(
        id = "1",
        name = "Player 1",
        score = 0,
        countriesGuessedCorrectly = listOf(),
        isItsTurn = true,
        email = ""
    )
    private val user2 = User(
        id = "2",
        name = "Player 2",
        score = 0,
        countriesGuessedCorrectly = listOf(),
        isItsTurn = false,
        email = ""
    )

    init {
        startGame()
    }

    private fun startGame() {
        viewModelScope.launch {
            val allCountries = getCountriesUseCase.invoke()
            gameServer.setCountries(countries = allCountries)
            gameServer.setPlayers(userOne = user1, userTwo = user2)
            gameServer.startGame()
            getGameStateData()
        }
    }

    private suspend fun getGameStateData() {
        gameState.collect { game ->
            _gamePageUiState.update { uiState ->
                uiState.copy(
                    state = game.gameStateLabel,
                    user1 = game.user1,
                    user2 = game.user2,
                    currentLetter = game.currentLetter,
                    numOfCountriesLeft = game.numOfCountriesLeft,
                    countriesRemaining = game.countriesRemaining,
                    remainingLetters = game.remainingLetters,
                    currentAnswer = game.currentAnswer,
                    searchBarText = game.currentAnswer,
                    player1TurnColor = getTurnColor(isPlayerTurn = game.user1.isItsTurn),
                    player2TurnColor = getTurnColor(isPlayerTurn = game.user2.isItsTurn),
                    resultBackgroundColor = Color.Green,//getResultBackgroundColor(),
                    resultText = getResultText(game.user1, game.user2),
                    gameWinner = getGameWinner(user1, user2),
                )
            }
        }
    }

    private fun getTurnColor(isPlayerTurn: Boolean): Color {
        return if (isPlayerTurn) Color.Yellow else Color.LightGray
    }

    private fun getResultBackgroundColor(playerId: String): Color {
        return if (playerId == "") Color.LightGray else Color.Yellow
    }

    private fun getResultText(user1: User, user2: User): String {
        val winner = if (user1.isRoundWinner) user1
        else if (user2.isRoundWinner) user2
        else User(name = "Both")
        return "Result: ${winner.name} Won!"
    }

    private fun getGameWinner(user1: User, user2: User): String {
        return if (user1.isGameWinner) user1.name
        else if (user2.isGameWinner) user2.name
        else ""
    }

    fun onPlayerAnswered(answer: String) { gameServer.onAnswerSubmitted(answer = answer) }

    fun onPlayerGaveUp() { gameServer.onGiveUp() }

    fun startNextRound() = gameServer.startNextRound()

    fun hideBottomSheet() { _gamePageUiState.update { it.copy(showBottomSheet = false) } }

    fun showBottomSheet(country: Country) {
        _gamePageUiState.update { uiState ->
            uiState.copy(
                showBottomSheet = true,
                bottomSheetViewData = CountryBottomSheetViewData(
                    countryName = country.name,
                    capital = country.capital,
                    region = country.region,
                    flag = country.flag,
                    maps = country.maps,
                    population = country.population,
                    unMember = country.unMember,
                    imgUrl = country.coatOfArms.png,
                    languages = country.languages.toString(),
                    currencies = country.currencies.toString(),
                    borders = country.borders.toString(),
                )
            )
        }
    }
}