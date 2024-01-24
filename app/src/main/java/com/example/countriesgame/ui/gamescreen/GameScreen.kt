package com.example.countriesgame.ui.gamescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    vm: GameScreenViewModel = hiltViewModel(),
    ) {

    when (val uiState = vm.gameScreenUiState) {
        is GameScreenUiState.GameOver -> {
            Text(text = "Game Over. Thank you for playing.")
        }
        is GameScreenUiState.Loading -> {
            Text(text = "Loading...")
        }
        is GameScreenUiState.RoundFinished -> {
            Text(text = "Round Completed!")
        }
        is GameScreenUiState.GameInProgress -> {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .padding(5.dp)
            ) {
                Text(
                    text = "The current letter is: ${uiState.currentLetter}",
                    fontSize = 30.sp,
                )
                Text(
                    text = "${uiState.numOfCountriesLeft} Countries Remaining!",
                    fontSize = 20.sp,
                )
                TextField(
                    value = uiState.keyboardText,
                    onValueChange = { country: String -> vm.onCountryGuessed(country) },
                    singleLine = true,
                    label = { Text(text = "Country") },
                )
                Row {
                    ScoreBoard(
                        name = uiState.player1Name,
                        turnColor = uiState.player1TurnColor,
                        score = uiState.player1Score,
                        countriesGuessedCorrectly = uiState.player1Countries,
                    )
                    ScoreBoard(
                        name = uiState.player2Name,
                        turnColor = uiState.player2TurnColor,
                        score = uiState.player2Score,
                        countriesGuessedCorrectly = uiState.player2Countries,
                    )
                }
                Button(onClick = { vm.updateStateOnGiveUp() }) { Text(text = "Give Up") }

                if (uiState.showCountriesRemaining) {
                    Column {
                        Text(text = "Missed Countries")
                        LazyColumn {
                            items(uiState.countriesRemaining) { country ->
                                Text(text = country)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBoard(
    name: String,
    turnColor: Color,
    score: Int,
    countriesGuessedCorrectly: List<String>,
) {
    Column(
        modifier = Modifier
            .background(turnColor)
    ) {
        Text(text = name)
        Text("Score: $score")
        LazyColumn {
            items(countriesGuessedCorrectly) { country ->
                Text(text = country)
            }
        }
    }
}