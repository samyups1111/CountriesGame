package com.example.countriesgame.ui.gamescreen.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countriesgame.model.Country
import com.example.countriesgame.ui.gamescreen.component.ScoreBoard
import com.example.countriesgame.ui.gamescreen.state.GameScreenUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundInProgressPage(
    gameScreenUiState: GameScreenUiState.RoundInProgress,
    onCountryGuessed: (String) -> Unit,
    showBottomSheet: (Country) -> Unit,
    onGiveUp: () -> Unit,
    modifier: Modifier = Modifier,
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(5.dp)
    ) {
        Text(
            text = "The current letter is: ${gameScreenUiState.currentLetter}",
            fontSize = 30.sp,
        )
        Text(
            text = "${gameScreenUiState.numOfCountriesLeft} Countries Remaining!",
            fontSize = 20.sp,
        )
        SearchBar(
            query = gameScreenUiState.searchBarText,
            onQueryChange = onCountryGuessed,
            onSearch = {},
            active = true,
            onActiveChange = {},
            placeholder = { Text("Country") },
            content = {},
            modifier = Modifier
                .shadow(elevation = 15.dp)
                .border(
                    color = Color.Blue,
                    width = 1.dp,
                    shape = CircleShape.copy(all = CornerSize(15.dp))
                )
                .background(
                    color = Color.White,
                    shape = CircleShape.copy(all = CornerSize(15.dp))
                )
                .padding(5.dp)
                .height(70.dp)
        )
        Row() {
            ScoreBoard(
                name = gameScreenUiState.user1.name,
                turnColor = gameScreenUiState.player1TurnColor,
                score = gameScreenUiState.user1.score,
                countriesGuessedCorrectly = gameScreenUiState.user1.countriesGuessedCorrectly,
                modifier = Modifier.weight(1F),
                showBottomSheet = showBottomSheet,
            )
            ScoreBoard(
                name = gameScreenUiState.user2.name,
                turnColor = gameScreenUiState.player2TurnColor,
                score = gameScreenUiState.user2.score,
                countriesGuessedCorrectly = gameScreenUiState.user2.countriesGuessedCorrectly,
                modifier = Modifier.weight(1F),
                showBottomSheet = showBottomSheet,
            )
        }
        Spacer(modifier = Modifier.weight(1F))
        Button(
            onClick = onGiveUp,
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Give Up")
        }
    }
}

//@Preview
//@Composable
//fun RoundInProgressPreview() {
//    CountriesGameTheme {
//        // A surface container using the 'background' color from the theme
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            RoundInProgressPage(
//                gameScreenUiState = GameScreenUiState.RoundInProgress(
//                    player1Name = "Sammy DJ",
//                    currentLetter = 's',
//                    numOfCountriesLeft = 22,
//                    player1Score = 3,
//                    player2Score = 1,
//                    player1TurnColor = Color.Yellow,
//                    player1Countries = listOf("South Korea", "Senegal", "Somalia"),
//                    player2Countries = listOf("South Africa"),
//                ),
//                showBottomSheetViaString = {},
//                onCountryGuessed = {},
//                onGiveUp = {},
//            )
//        }
//    }
//}