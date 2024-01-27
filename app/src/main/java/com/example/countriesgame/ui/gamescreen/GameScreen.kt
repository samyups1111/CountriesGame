package com.example.countriesgame.ui.gamescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.countriesgame.model.CoatOfArms
import com.example.countriesgame.model.Country
import com.example.countriesgame.model.CountryMap
import com.example.countriesgame.model.CountryName
import com.example.countriesgame.ui.theme.CountriesGameTheme

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    vm: GameScreenViewModel = hiltViewModel(),
) {
    GameScreenContent(
        uiState = vm.gameScreenUiState,
        bottomSheetState = vm.bottomSheetState,
        onCountryGuessed = { country: String -> vm.onCountryGuessed(countryGuessed = country) },
        onGiveUp = { vm.updateStateOnGiveUp() },
        startNextRound = { vm.startNextRound() },
        showBottomSheet = { country: Country ->  vm.showBottomSheet(country) },
        showBottomSheetViaString = { country: String ->  vm.showBottomSheet(country) },
        hideBottomSheet = { vm.hideBottomSheet() },
        modifier = modifier,
    )
}

@Composable
fun GameScreenContent(
    uiState: GameScreenUiState,
    modifier: Modifier = Modifier,
    bottomSheetState: BottomSheetState = BottomSheetState.Hide,
    onCountryGuessed: (String) -> Unit = {},
    onGiveUp: () -> Unit = {},
    startNextRound: () -> Unit = {},
    showBottomSheet: (Country) -> Unit = {},
    showBottomSheetViaString: (String) -> Unit = {},
    hideBottomSheet: () -> Unit = {},
    ) {

    when (uiState) {
        is GameScreenUiState.GameOver -> {
            Text(text = "Game Over. Thank you for playing.")
        }
        is GameScreenUiState.Loading -> {
            Text(text = "Loading...")
        }
        is GameScreenUiState.RoundFinished -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Text(
                    text = uiState.result,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(10.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = CircleShape.copy(all = CornerSize(15.dp))
                        )
                        .background(
                            color = uiState.resultBackgroundColor,
                            shape = CircleShape.copy(all = CornerSize(15.dp))
                        )
                        .padding(15.dp)
                )
                Row() {
                    ScoreBoard(
                        name = uiState.player1Name,
                        turnColor = Color.Yellow,
                        score = uiState.player1Score,
                        countriesGuessedCorrectly = emptyList(),
                        modifier = Modifier.weight(1F),
                        showBottomSheet = showBottomSheetViaString,
                    )
                    ScoreBoard(
                        name = uiState.player2Name,
                        turnColor = Color.Red,
                        score = uiState.player2Score,
                        countriesGuessedCorrectly = emptyList(),
                        modifier = Modifier.weight(1F),
                        showBottomSheet = showBottomSheetViaString,
                    )
                }

                if (uiState.missedCountries.isNotEmpty()) {
                    Text(
                        text = "Missed Countries",
                        fontWeight = FontWeight.Bold,
                    )
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        items(uiState.missedCountries) { country ->
                            Text(
                                text = country.name.common,
                                modifier = Modifier
                                    .clickable(
                                        enabled = true,
                                    ) { showBottomSheet(country) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1F))
                Button(
                    onClick = startNextRound,
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Text(text = "Start Next Round")
                }
                if (bottomSheetState is BottomSheetState.Show) {
                    CountryBottomSheet(
                        officialName = bottomSheetState.countryName.official,
                        commonName = bottomSheetState.countryName.common,
                        capital = bottomSheetState.capital.first(),
                        population = bottomSheetState.population,
                        region = bottomSheetState.region,
                        flag = bottomSheetState.flag,
                        maps = bottomSheetState.maps,
                        unMember = bottomSheetState.unMember,
                        hideBottomSheet = hideBottomSheet,
                        imgUrl = bottomSheetState.imgUrl,
                        languages = bottomSheetState.languages,
                        borders = bottomSheetState.borders,
                        currencies = bottomSheetState.currencies,
                    )
                }
            }
        }
        is GameScreenUiState.RoundInProgress -> {

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
                    onValueChange = onCountryGuessed,
                    singleLine = true,
                    label = { Text(text = "Country") },
                    modifier = Modifier
                        .padding(10.dp)
                )
                Row() {
                    ScoreBoard(
                        name = uiState.player1Name,
                        turnColor = uiState.player1TurnColor,
                        score = uiState.player1Score,
                        countriesGuessedCorrectly = uiState.player1Countries,
                        modifier = Modifier.weight(1F),
                        showBottomSheet = showBottomSheetViaString
                    )
                    ScoreBoard(
                        name = uiState.player2Name,
                        turnColor = uiState.player2TurnColor,
                        score = uiState.player2Score,
                        countriesGuessedCorrectly = uiState.player2Countries,
                        modifier = Modifier.weight(1F),
                        showBottomSheet = showBottomSheetViaString,
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
                Button(
                    onClick = onGiveUp,
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Text(text = "Give Up")
                }
                if (bottomSheetState is BottomSheetState.Show) {
                    CountryBottomSheet(
                        officialName = bottomSheetState.countryName.official,
                        commonName = bottomSheetState.countryName.common,
                        capital = bottomSheetState.capital.first(),
                        population = bottomSheetState.population,
                        region = bottomSheetState.region,
                        flag = bottomSheetState.flag,
                        maps = bottomSheetState.maps,
                        unMember = bottomSheetState.unMember,
                        hideBottomSheet = hideBottomSheet,
                        imgUrl = bottomSheetState.imgUrl,
                        languages = bottomSheetState.languages,
                        borders = bottomSheetState.borders,
                        currencies = bottomSheetState.currencies,
                    )
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
    showBottomSheet: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape.copy(all = CornerSize(15.dp)),
            )
            .background(
                color = turnColor,
                shape = CircleShape.copy(all = CornerSize(15.dp)),
            )
            .padding(20.dp)

    ) {
        Text(
            text = name,
            fontSize = 15.sp,
            modifier = Modifier
        )
        Text(
            text = "Score: $score",
        )
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(countriesGuessedCorrectly) { country ->
                Text(
                    text = country,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(7.dp)
                        .clickable(
                            enabled = true,
                        ) { showBottomSheet(country) }
                )
            }
        }
    }
}

@Preview
@Composable
fun RoundInProgressPreview() {
    CountriesGameTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GameScreenContent(
                uiState = GameScreenUiState.RoundInProgress(
                    player1Name = "Sammy DJ",
                    currentLetter = 's',
                    numOfCountriesLeft = 22,
                    player1Score = 3,
                    player2Score = 1,
                    player1TurnColor = Color.Green,
                    player1Countries = listOf("South Korea", "Senegal", "Somalia"),
                    player2Countries = listOf("South Africa"),
                ),
            )
        }
    }
}

@Preview
@Composable
fun RoundFinishedPreview() {
    CountriesGameTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GameScreenContent(
                uiState = GameScreenUiState.RoundFinished(
                    player1Name = "Sammy DJ",
                    result = "Sammy DJ won that round!",
                    currentLetter = 'c',
                    missedCountries = listOf(
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
                    player1Score = 2,
                    player2Score = 1,
                    remainingLetters = listOf('b', 'f', 'p'),
                    resultBackgroundColor = Color.Green,
                )
            )
        }
    }
}