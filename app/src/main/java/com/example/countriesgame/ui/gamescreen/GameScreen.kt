package com.example.countriesgame.ui.gamescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.countriesgame.ui.theme.CountriesGameTheme

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    vm: GameScreenViewModel = hiltViewModel(),
) {
    GameScreenContent(
        uiState = vm.gameScreenUiState,
        onCountryGuessedCorrectly = { country: String -> vm.onCountryGuessed(country = country) },
        updateStateOnGiveUp = { vm.updateStateOnGiveUp() },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenContent(
    uiState: GameScreenUiState,
    modifier: Modifier = Modifier,
    onCountryGuessedCorrectly: (String) -> Unit = {},
    updateStateOnGiveUp: () -> Unit = {},
    ) {

    when (uiState) {
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
                    onValueChange = onCountryGuessedCorrectly,
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
                Button(onClick = updateStateOnGiveUp) { Text(text = "Give Up") }

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

@Preview
@Composable
fun GameScreenPreview() {
    CountriesGameTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GameScreenContent(
                uiState = GameScreenUiState.GameInProgress(
                    player1Name = "Sammy DJ",
                    currentLetter = 's',
                    player1Score = 3,
                    player2Score = 1,
                    player1TurnColor = Color.Green,
                    player1Countries = listOf("South Korea", "Senegal", "Somalia"),
                    player2Countries = listOf("South Africa"),
                )
            )
        }
    }

}