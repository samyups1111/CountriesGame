package com.example.countriesgame.ui.gamescreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.countriesgame.ui.gamescreen.page.GamePage

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    vm: GameScreenViewModel = hiltViewModel(),
) {
    val gameState by vm.gamePageUiState.collectAsStateWithLifecycle()

    GamePage(
        uiState = gameState,
        showBottomSheet = { country -> vm.showBottomSheet(country) },
        onCountryGuessed = { answer -> vm.onPlayerAnswered(answer) },
        onGiveUp = { vm.onPlayerGaveUp() },
        startNextRound = { vm.startNextRound() },
        hideBottomSheet = { vm.hideBottomSheet() },
        modifier = modifier,
    )
}