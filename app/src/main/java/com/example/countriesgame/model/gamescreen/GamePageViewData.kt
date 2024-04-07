package com.example.countriesgame.model.gamescreen

import androidx.compose.ui.graphics.Color
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.User

data class GamePageViewData(
    val state: GameStateLabel = GameStateLabel.LOADING,
    val user1: User = User(),
    val user2: User = User(),
    val currentLetter: Char = ' ',
    val countriesRemaining: List<Country> = emptyList(),
    val numOfCountriesLeft: Int = 100,
    val player1TurnColor: Color = Color.LightGray,
    val player2TurnColor: Color = Color.LightGray,
    val searchBarText: String = "",
    val remainingLetters: List<Char> = emptyList(),
    val showBottomSheet: Boolean = false,
    val bottomSheetViewData: CountryBottomSheetViewData = CountryBottomSheetViewData(),
    val currentAnswer: String = "",
    val resultText: String = "",
    val resultBackgroundColor: Color = Color.Black,
    val gameWinner: String = "",
)

sealed class GameStateLabel {
    object LOADING : GameStateLabel()
    object GAME_IN_PROGRESS : GameStateLabel()
    object BETWEEN_ROUNDS : GameStateLabel()
    object GAME_OVER : GameStateLabel()
}
