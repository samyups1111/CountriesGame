package com.example.countriesgame.ui.gamescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.GetCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
): ViewModel() {

    var gameScreenUiState by mutableStateOf<GameScreenUiState>(GameScreenUiState.Loading)
        private set

    var bottomSheetState by mutableStateOf<BottomSheetState>(BottomSheetState.Hide)

    private var gameInProgressState = GameScreenUiState.RoundInProgress()

    private var allCountries: List<Country> = emptyList()

    init {
        startGame()
    }

    private fun startGame() {

        viewModelScope.launch {
            allCountries = getCountriesUseCase.invoke()

            val currentLetter = GameScreenUiState.qualifiedLetters.random()
            val countriesRemaining = allCountries.filter { it.name.common.first() == currentLetter }

            gameScreenUiState = GameScreenUiState.RoundInProgress(
                currentLetter = currentLetter,
                countriesRemaining = countriesRemaining,
                numOfCountriesLeft = countriesRemaining.size,
                player1TurnColor = Color.Green,
            )
        }
    }

    fun onCountryGuessed(countryGuessed: String) {

        val state = gameScreenUiState as GameScreenUiState.RoundInProgress
        updateKeyboard(countryGuessed, state)

        state.countriesRemaining.forEach { country ->
            val countryGuessedFormatted = countryGuessed.trim()
            val commonNameList = country.name.common.split(',')
            if (country.name.official.equals(countryGuessedFormatted, ignoreCase = true) ||
                commonNameList.any {  it.equals(countryGuessedFormatted, ignoreCase = true) }) {
                onGuessedCorrectly(country.name.common, state)
            }
        }
    }

    private fun onGuessedCorrectly(countryName: String, prevState: GameScreenUiState.RoundInProgress) {

        val numOfCountriesRemaining = prevState.numOfCountriesLeft - 1

        if (numOfCountriesRemaining < 1) {
            setRoundFinishedState(prevState)
        } else {
            gameScreenUiState = prevState.copy(
                countriesRemaining = prevState.countriesRemaining.filter { it.name.common != countryName && it.name.official != countryName},
                player1Countries = addCountryToCorrectlyGuessedList(country = countryName, Players.Player1),
                player2Countries = addCountryToCorrectlyGuessedList(country = countryName, Players.Player2),
                keyboardText = "",
                numOfCountriesLeft = numOfCountriesRemaining,
                isPlayer1Turn = !prevState.isPlayer1Turn,
                player1TurnColor = getTurnColor(
                    isPlayer1Turn = !prevState.isPlayer1Turn,
                    player = Players.Player1,
                ),
                player2TurnColor = getTurnColor(
                    isPlayer1Turn = !prevState.isPlayer1Turn,
                    player = Players.Player2,
                ),
            )
        }
    }

    fun updateStateOnGiveUp() {
        val prevState = gameScreenUiState as GameScreenUiState.RoundInProgress

        setRoundFinishedState(prevState, awardPoint = true)
    }

    private fun setRoundFinishedState(prevState: GameScreenUiState.RoundInProgress, awardPoint: Boolean = false) {

        if (prevState.remainingLetters.isEmpty()) {
            gameScreenUiState = GameScreenUiState.GameOver
            return
        }

        val player1Score = if (awardPoint && prevState.isPlayer1Turn) prevState.player1Score else prevState.player1Score
        val player2Score = if (awardPoint && prevState.isPlayer1Turn) prevState.player2Score + 1 else prevState.player2Score
        val currentLetter = prevState.remainingLetters.random()
        val missedCountriesPrevRound = prevState.countriesRemaining
        val countriesRemainingThisRound = allCountries.filter { it.name.common.first() == currentLetter }
        val numOfCountriesRemaining = countriesRemainingThisRound.size
        val remainingLetters = prevState.remainingLetters.filter { it != currentLetter }
        val isPlayer1Turn = !prevState.isPlayer1Turn
        val result = if (prevState.isPlayer1Turn) "${prevState.player2Name} won that round!" else "${prevState.player1Name} won that round!"

        gameScreenUiState = GameScreenUiState.RoundFinished(
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
        )
    }

    private fun saveInProgressState(
        prevState: GameScreenUiState.RoundInProgress,
        countriesRemaining: List<Country>,
        numOfCountriesRemaining: Int,
        isPlayer1Turn: Boolean,
        player1Score: Int,
        player2Score: Int,
        currentLetter: Char,
    ) {
        gameInProgressState = prevState.copy(
            countriesRemaining = countriesRemaining,
            player1Score = player1Score,
            player2Score = player2Score,
            player1Countries = emptyList(),
            player2Countries = emptyList(),
            currentLetter = currentLetter,
            keyboardText = "",
            numOfCountriesLeft = numOfCountriesRemaining,
            isPlayer1Turn = isPlayer1Turn,
            player1TurnColor = getTurnColor(
                isPlayer1Turn = isPlayer1Turn,
                player = Players.Player1,
            ),
            player2TurnColor = getTurnColor(
                isPlayer1Turn = isPlayer1Turn,
                player = Players.Player2,
            ),
        )
    }

    private fun addCountryToCorrectlyGuessedList(country: String, player: Players): List<String> {

        val state = gameScreenUiState as GameScreenUiState.RoundInProgress

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
        gameScreenUiState = gameInProgressState
    }

    private fun updateKeyboard(countryName: String, state: GameScreenUiState.RoundInProgress) {
        gameScreenUiState = state.copy(keyboardText = countryName)
    }

    private fun getTurnColor(isPlayer1Turn: Boolean, player: Players): Color {
        return when (player) {
            is Players.Player1 -> {
                if (isPlayer1Turn) Color.Green else Color.LightGray
            }
            is Players.Player2 -> {
                if (isPlayer1Turn) Color.LightGray else Color.Green
            }
        }
    }

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
            )
    }
    fun showBottomSheet(countryName: String) {
        val country = getFilteredCountry(countryName)
        showBottomSheet(country)
    }

    private fun getFilteredCountry(name: String): Country {
        return allCountries.first { it.name.official == name || it.name.common.split(',').any { commonName -> commonName == name } }
    }
}