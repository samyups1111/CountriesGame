package com.example.countriesgame.ui.gamescreen.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countriesgame.model.CoatOfArms
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.CountryMap
import com.example.countriesgame.model.CountryName
import com.example.countriesgame.model.User
import com.example.countriesgame.ui.gamescreen.component.ScoreBoard
import com.example.countriesgame.ui.gamescreen.viewdata.GamePageViewData

@Composable
fun RoundFinishedPage(
    roundFinishedState: GamePageViewData,
    showBottomSheet: (Country) -> Unit,
    startNextRound: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = roundFinishedState.resultText,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(10.dp)
                .shadow(
                    elevation = 5.dp,
                    shape = CircleShape.copy(all = CornerSize(15.dp))
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape.copy(all = CornerSize(15.dp))
                )
                .background(
                    color = roundFinishedState.resultBackgroundColor,
                    shape = CircleShape.copy(all = CornerSize(10.dp))
                )
                .padding(15.dp)
        )
        Row {
            ScoreBoard(
                name = roundFinishedState.user1.name,
                turnColor = Color.LightGray,
                score = roundFinishedState.user1.score,
                countriesGuessedCorrectly = emptyList(),
                modifier = Modifier.weight(1F),
                showBottomSheet = showBottomSheet,
            )
            ScoreBoard(
                name = roundFinishedState.user2.name,
                turnColor = Color.LightGray,
                score = roundFinishedState.user2.score,
                countriesGuessedCorrectly = emptyList(),
                modifier = Modifier.weight(1F),
                showBottomSheet = showBottomSheet,
            )
        }

        if (roundFinishedState.countriesRemaining.isNotEmpty()) {
            Text(
                text = "Missed Countries",
                fontWeight = FontWeight.Bold,
            )
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1F)
            ) {
                items(roundFinishedState.countriesRemaining) {country ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .shadow(elevation = 15.dp)
                            .border(
                                width = 1.dp,
                                shape = CircleShape.copy(all = CornerSize(15.dp)),
                                color = Color.Black
                            )
                            .padding(8.dp)
                            .clickable(
                                enabled = true,
                            ) { showBottomSheet(country) }
                    ) {
                        Row {
                            Text(text = country.flag)
                            Text(text = country.name.common)
                        }
                    }

                }
            }
        }
        //Spacer(modifier = Modifier.weight(1F))
        Button(
            onClick = startNextRound,
            modifier = Modifier
                .padding(5.dp)
        ) {
            Text(text = "Start Next Round")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoundFinishedPreview() {
    RoundFinishedPage(
        roundFinishedState = GamePageViewData(
            user1 = User(
                id = "",
                name = "sam",
                score = 3,
                countriesGuessedCorrectly = emptyList(),
                isItsTurn = true
            ),
            user2 = User(
                id = "",
                name = "Angel",
                score = 1,
                countriesGuessedCorrectly = emptyList(),
                isItsTurn = false,
            ),
            currentLetter = 'c',
            countriesRemaining = listOf(
                Country(
                    id = "",
                    name = CountryName("United States", "USA"),
                    capital = listOf("capital"),
                    region = "Americas",
                    borders = listOf("USA"),
                    coatOfArms = CoatOfArms("", ""),
                    currencies = listOf("", "cad"),
                    flag = "",
                    maps = CountryMap("", ""),
                    languages = listOf(""),
                    population = 100000,
                    unMember = true,
                ),
                Country(
                    id = "",
                    name = CountryName("Canada", "Canada"),
                    capital = listOf("capital"),
                    region = "Americas",
                    borders = listOf("USA"),
                    coatOfArms = CoatOfArms("", ""),
                    currencies = listOf("", "cad"),
                    flag = "",
                    maps = CountryMap("", ""),
                    languages = listOf(""),
                    population = 100000,
                    unMember = true,
                ),
            ),
            remainingLetters = listOf('b', 'f', 'p'),
            resultBackgroundColor = Color.Green,
        ),
        showBottomSheet = {},
        startNextRound = {},
        modifier = Modifier.fillMaxSize()
    )
}