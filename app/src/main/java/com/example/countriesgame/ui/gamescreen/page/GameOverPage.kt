package com.example.countriesgame.ui.gamescreen.page

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.countriesgame.ui.gamescreen.state.GameState

@Composable
fun GameOverPage(
    gameState: GameState.GameOver,
    modifier: Modifier = Modifier,
) {
    Text(text = "Congratulations ${gameState.winner}")
}