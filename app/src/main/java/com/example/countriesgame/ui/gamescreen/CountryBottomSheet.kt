package com.example.countriesgame.ui.gamescreen

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.countriesgame.model.CountryMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryBottomSheet(
    officialName: String,
    commonName: String,
    capital: String,
    region: String,
    flag: String,
    maps: CountryMap,
    population: Int,
    unMember: Boolean,
    modifier: Modifier = Modifier,
    hideBottomSheet: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        modifier = modifier
    ) {
        Text(
            text = flag + officialName + "\n" + commonName,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .align(CenterHorizontally)
        )
        Row {
            Text(text = "Capital: ")
            Text(text = capital)
        }
        Row {
            Text(text = "Located in: ")
            Text(text = region)
        }
        Row {
            Text(text = "Population: ")
            Text(text = population.toString())
        }
        Row {
            Text(text = "United Nations Member?: ")
            Text(text = unMember.toString())
        }
        Row {
            Text(text = "Search in Google Maps: ")
            Text(text = maps.googleMaps)
        }
    }
}

//@Preview
//@Composable
//fun CountryBottomSheetPreview() {
//    CountriesGameTheme {
//        Surface(
//            color = MaterialTheme.colorScheme.background
//        ) {
//            CountryBottomSheet(
//                officialName = "United States",
//            )
//        }
//    }
//}