package com.example.countriesgame.ui.gamescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.Player
import com.example.countriesgame.model.usecase.GetCountriesUseCase
import com.example.countriesgame.server.GameServer
import com.example.countriesgame.server.toGameScreenUiState
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

    private val player1 = Player(
        id = 1,
        name = "Player 1",
        score = 0,
        countriesGuessedCorrectly = listOf(),
        isItsTurn = true
    )
    private val player2 = Player(
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
            gameServer.setCountries(allCountries)
            gameServer.startGame(
                player1,
                player2,
            )
        }
    }

    fun onPlayerAnswered(answer: String) {
        gameServer.onAnswerSubmitted(answer)
    }

    fun onPlayerGaveUp() {
        gameServer.onGiveUp()
    }

    fun startNextRound() = gameServer.startNextRound()

    fun hideBottomSheet() {
        bottomSheetState = BottomSheetState.Hide
    }

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
}