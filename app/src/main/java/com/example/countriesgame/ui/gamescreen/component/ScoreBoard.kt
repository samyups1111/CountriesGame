package com.example.countriesgame.ui.gamescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countriesgame.model.Country

@Composable
fun ScoreBoard(
    name: String,
    turnColor: Color,
    score: Int,
    countriesGuessedCorrectly: List<Country>,
    showBottomSheet: (Country) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(10.dp)
            .shadow(
                elevation = 15.dp,
                shape = CircleShape.copy(all = CornerSize(15.dp)),
            )
            .border(
                color = Color.Black,
                width = 1.dp,
                shape = CircleShape.copy(all = CornerSize(15.dp))
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
                    text = country.name.common,
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