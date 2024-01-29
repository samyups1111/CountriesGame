package com.example.countriesgame.ui.gamescreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.countriesgame.model.Country
import com.example.countriesgame.ui.gamescreen.state.BottomSheetState
import com.example.countriesgame.ui.gamescreen.state.GameState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.countriesgame.ui.gamescreen.component.CountryBottomSheet
import com.example.countriesgame.ui.gamescreen.page.GameOverPage
import com.example.countriesgame.ui.gamescreen.page.RoundFinishedPage
import com.example.countriesgame.ui.gamescreen.page.RoundInProgressPage

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    vm: GameScreenViewModel = hiltViewModel(),
) {
    val gameState by vm.gameStateFlow.collectAsStateWithLifecycle()
    val bottomSheetState = vm.bottomSheetState

    when (gameState) {
        is GameState.GameOver -> {
            GameOverPage(
                gameState = gameState as GameState.GameOver,
            )
        }
        is GameState.Loading -> {
            Text(text = "Loading...")
        }
        is GameState.RoundFinished -> {
            RoundFinishedPage(
                roundFinishedState = gameState as GameState.RoundFinished,
                showBottomSheetViaString = { countryName: String -> vm.showBottomSheet(countryName) },
                showBottomSheet = { country: Country -> vm.showBottomSheet(country) },
                startNextRound = { vm.startNextRound() },
                modifier = modifier,
            )
        }
        is GameState.RoundInProgress -> {
            RoundInProgressPage(
                gameState = gameState as GameState.RoundInProgress,
                onCountryGuessed = { answer: String -> vm.onPlayerAnswered(answer) },
                showBottomSheetViaString = { countryName: String -> vm.showBottomSheet(countryName) },
                onGiveUp = { vm.onPlayerGaveUp() },
                modifier = modifier,
            )
        }
    }
    if (bottomSheetState is BottomSheetState.Show) {
        CountryBottomSheet(
            state = bottomSheetState,
            hideBottomSheet = { vm.hideBottomSheet() },
        )
    }
}