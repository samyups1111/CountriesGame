package com.example.countriesgame.ui.gamescreen.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.gamescreen.GamePageViewData
import com.example.countriesgame.model.gamescreen.GameStateLabel
import com.example.countriesgame.ui.gamescreen.component.CountryBottomSheet

@Composable
fun GamePage(
    uiState: GamePageViewData,
    showBottomSheet: (Country) -> Unit,
    onGiveUp: () -> Unit,
    onCountryGuessed: (String) -> Unit,
    startNextRound: () -> Unit,
    hideBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        when (uiState.state) {
            GameStateLabel.LOADING -> {
                Text(text = "Loading...")
            }
            GameStateLabel.GAME_IN_PROGRESS -> {
                RoundInProgressPage(
                    gameScreenUiState = uiState,
                    onCountryGuessed = onCountryGuessed,
                    showBottomSheet = showBottomSheet,
                    onGiveUp = onGiveUp,
                    modifier = modifier,
                )
            }
            GameStateLabel.BETWEEN_ROUNDS -> {
                RoundFinishedPage(
                    roundFinishedState = uiState,
                    showBottomSheet = showBottomSheet,
                    startNextRound = startNextRound,
                )
            }
            GameStateLabel.GAME_OVER -> {
                GameOverPage(gameScreenUiState = uiState)
            }
        }
        if (uiState.showBottomSheet) {
            CountryBottomSheet(
                data = uiState.bottomSheetViewData,
                hideBottomSheet = hideBottomSheet,
            )
        }
    }
}