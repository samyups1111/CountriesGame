package com.example.countriesgame.ui.gamescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.User
import com.example.countriesgame.model.usecase.GetCountriesUseCase
import com.example.countriesgame.server.GameServer
import com.example.countriesgame.server.GameState
import com.example.countriesgame.ui.gamescreen.state.BottomSheetState
import com.example.countriesgame.ui.gamescreen.state.GameScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val gameServer: GameServer,
): ViewModel() {

    val gameScreenUiStateFlow: StateFlow<GameScreenUiState> = gameServer.gameState.map { gameState ->
        gameState.toGameScreenUiState()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GameScreenUiState.Loading,
    )
    var bottomSheetState by mutableStateOf<BottomSheetState>(BottomSheetState.Hide)
        private set

    private val user1 = User(
        id = 1,
        name = "Player 1",
        score = 0,
        countriesGuessedCorrectly = listOf(),
        isItsTurn = true
    )
    private val user2 = User(
        id = 2,
        name = "Player 2",
        score = 0,
        countriesGuessedCorrectly = listOf(),
        isItsTurn = false
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
        }
    }

    fun onPlayerAnswered(answer: String) { gameServer.onAnswerSubmitted(answer = answer) }

    fun onPlayerGaveUp() { gameServer.onGiveUp() }

    fun startNextRound() = gameServer.startNextRound()

    fun hideBottomSheet() { bottomSheetState = BottomSheetState.Hide }

    fun showBottomSheet(country: Country) {
        bottomSheetState = BottomSheetState.Show(
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
    }

    private fun GameState.toGameScreenUiState(): GameScreenUiState {
        return when (this) {
            is GameState.GameOver -> { GameScreenUiState.GameOver(winner = this.winner.name) }

            is GameState.Loading -> { GameScreenUiState.Loading }

            is GameState.RoundInProgress -> {
                GameScreenUiState.RoundInProgress(
                    user1 = user1,
                    user2 = user2,
                    currentLetter = this.currentLetter,
                    countriesRemaining = this.countriesRemaining,
                    numOfCountriesLeft = this.numOfCountriesLeft,
                    searchBarText = this.currentAnswer,
                    remainingLetters = this.remainingLetters,
                    player1TurnColor = getPlayer1TurnColor(isPlayer1Turn = this.user1.isItsTurn),
                    player2TurnColor = getPlayer2TurnColor(isPlayer2Turn = this.user2.isItsTurn),
                )
            }

            is GameState.RoundFinished -> {
                GameScreenUiState.RoundFinished(
                    user1 = user1,
                    user2 = user2,
                    currentLetter = this.currentLetter,
                    missedCountries = this.missedCountries,
                    numOfMissedCountries = this.numOfMissedCountries,
                    remainingLetters = this.remainingLetters,
                    resultText = getResultText(winnerName = this.roundWinner.name),
                    resultBackgroundColor = getResultBackgroundColor(playerId = this.roundWinner.id),
                )
            }
        }
    }

    private fun getPlayer1TurnColor(isPlayer1Turn: Boolean): Color {
        return if (isPlayer1Turn) Color.Yellow else Color.LightGray
    }

    private fun getPlayer2TurnColor(isPlayer2Turn: Boolean): Color {
        return if (isPlayer2Turn) Color.Yellow else Color.LightGray
    }

    private fun getResultText(winnerName: String): String {
        return "Result: $winnerName Won!"
    }

    private fun getResultBackgroundColor(playerId: Int): Color {
        return if (playerId == 0) Color.LightGray else Color.Yellow
    }
}