package com.example.countriesgame.ui.gamescreen.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.countriesgame.model.gamescreen.GamePageViewData
import com.example.countriesgame.ui.LottieLoader

@Composable
fun GameOverPage(
    gameScreenUiState: GamePageViewData,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Text(
            text = "Congratulations ${gameScreenUiState.gameWinner}",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
        LottieLoader(
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameOverPagePreview() {
    GameOverPage(
        gameScreenUiState = GamePageViewData(
            gameWinner = "Sammy"
        ),
    )
}