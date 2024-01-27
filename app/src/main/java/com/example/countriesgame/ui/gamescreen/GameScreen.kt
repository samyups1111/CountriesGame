package com.example.countriesgame.ui.gamescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import com.example.countriesgame.ui.gamescreen.state.BottomSheetState
import com.example.countriesgame.ui.gamescreen.state.CountryGameState
import com.example.countriesgame.ui.theme.CountriesGameTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    vm: GameScreenViewModel = hiltViewModel(),
) {
    val gameState = vm.countryGameStateFlow.collectAsStateWithLifecycle()
    val bottomSheetState = vm.bottomSheetState

    GameScreenContent(
        countryGameState = gameState.value,
        onCountryGuessed = { country: String -> vm.onPlayerAnswered(countryGuessed = country) },
        onGiveUp = { vm.onPlayerGaveUp() },
        startNextRound = { vm.startNextRound() },
        showBottomSheet = { country: Country ->  vm.showBottomSheet(country) },
        showBottomSheetViaString = { country: String ->  vm.showBottomSheet(country) },
        modifier = modifier,
    )
    if (bottomSheetState is BottomSheetState.Show) {
        CountryBottomSheet(
            state = bottomSheetState,
            hideBottomSheet = { vm.hideBottomSheet() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameScreenContent(
    countryGameState: CountryGameState,
    modifier: Modifier = Modifier,
    onCountryGuessed: (String) -> Unit = {},
    onGiveUp: () -> Unit = {},
    startNextRound: () -> Unit = {},
    showBottomSheet: (Country) -> Unit = {},
    showBottomSheetViaString: (String) -> Unit = {},
    ) {

    when (countryGameState) {
        is CountryGameState.CountryGameOver -> {
            Text(text = "Game Over. Thank you for playing.")
        }
        is CountryGameState.Loading -> {
            Text(text = "Loading...")
        }
        is CountryGameState.RoundFinished -> {
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = modifier
            ) {
                Text(
                    text = countryGameState.result,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(10.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = CircleShape.copy(all = CornerSize(15.dp))
                        )
                        .background(
                            color = countryGameState.resultBackgroundColor,
                            shape = CircleShape.copy(all = CornerSize(15.dp))
                        )
                        .padding(15.dp)
                )
                Row() {
                    ScoreBoard(
                        name = countryGameState.player1Name,
                        turnColor = Color.LightGray,
                        score = countryGameState.player1Score,
                        countriesGuessedCorrectly = emptyList(),
                        modifier = Modifier.weight(1F),
                        showBottomSheet = showBottomSheetViaString,
                    )
                    ScoreBoard(
                        name = countryGameState.player2Name,
                        turnColor = Color.LightGray,
                        score = countryGameState.player2Score,
                        countriesGuessedCorrectly = emptyList(),
                        modifier = Modifier.weight(1F),
                        showBottomSheet = showBottomSheetViaString,
                    )
                }

                if (countryGameState.missedCountries.isNotEmpty()) {
                    Text(
                        text = "Missed Countries",
                        fontWeight = FontWeight.Bold,
                    )
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        items(countryGameState.missedCountries) { country ->
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

            }
        }
        is CountryGameState.RoundInProgress -> {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .padding(5.dp)
            ) {
                Text(
                    text = "The current letter is: ${countryGameState.currentLetter}",
                    fontSize = 30.sp,
                )
                Text(
                    text = "${countryGameState.numOfCountriesLeft} Countries Remaining!",
                    fontSize = 20.sp,
                )
                SearchBar(
                    query = countryGameState.searchBarText,
                    onQueryChange = onCountryGuessed,
                    onSearch = {},
                    active = true,
                    onActiveChange = {},
                    placeholder = { Text("Country") },
                    content = {},
                    modifier = Modifier
                        .padding(10.dp)
                        .height(75.dp)
                        .border(
                            color = Color.Blue,
                            width = 2.dp,
                            shape = CircleShape.copy(all = CornerSize(15.dp))
                        )
                ) 
                Row() {
                    ScoreBoard(
                        name = countryGameState.player1Name,
                        turnColor = countryGameState.player1TurnColor,
                        score = countryGameState.player1Score,
                        countriesGuessedCorrectly = countryGameState.player1Countries,
                        modifier = Modifier.weight(1F),
                        showBottomSheet = showBottomSheetViaString
                    )
                    ScoreBoard(
                        name = countryGameState.player2Name,
                        turnColor = countryGameState.player2TurnColor,
                        score = countryGameState.player2Score,
                        countriesGuessedCorrectly = countryGameState.player2Countries,
                        modifier = Modifier.weight(1F),
                        showBottomSheet = showBottomSheetViaString,
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
                Button(
                    onClick = onGiveUp,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(CenterHorizontally)
                ) {
                    Text(text = "Give Up")
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
                countryGameState = CountryGameState.RoundInProgress(
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
                countryGameState = CountryGameState.RoundFinished(
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