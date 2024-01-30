package com.example.countriesgame.ui.gamescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.server.GameServer
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.usecase.GetCountriesUseCase
import com.example.countriesgame.ui.gamescreen.state.BottomSheetState
import com.example.countriesgame.ui.gamescreen.state.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val gameServer: GameServer,
): ViewModel() {

    var gameStateFlow: MutableStateFlow<GameState> = gameServer.countryGameState
        private set

    var bottomSheetState by mutableStateOf<BottomSheetState>(BottomSheetState.Hide)
        private set

    private var allCountries: List<Country> = emptyList()

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch {
            allCountries = getCountriesUseCase.invoke()
            loadCountriesIntoServer()
            startGame()
        }
    }

    private fun startGame() = gameServer.startGame()

    private fun loadCountriesIntoServer() = gameServer.loadCountries(allCountries)

    fun onPlayerAnswered(countryGuessed: String) = gameServer.onAnswerSubmitted(countryGuessed)

    fun onPlayerGaveUp() = gameServer.updateStateOnGiveUp()

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
    fun showBottomSheet(countryName: String) {
        val country = getCountryByName(countryName)
        showBottomSheet(country)
    }

    private fun getCountryByName(name: String): Country {
        return allCountries.first { it.name.official == name || it.name.common.split(',').any { commonName -> commonName == name } }
    }
}