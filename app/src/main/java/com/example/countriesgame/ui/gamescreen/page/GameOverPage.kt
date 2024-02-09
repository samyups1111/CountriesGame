package com.example.countriesgame.ui.gamescreen.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.countriesgame.ui.LottieLoader
import com.example.countriesgame.ui.gamescreen.state.GameScreenUiState

@Composable
fun GameOverPage(
    gameScreenUiState: GameScreenUiState.GameOver,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Text(
            text = "Congratulations ${gameScreenUiState.winner}",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
        LottieLoader(
            modifier = modifier,
        )
    }
}