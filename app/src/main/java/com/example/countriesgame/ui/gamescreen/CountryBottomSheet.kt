package com.example.countriesgame.ui.gamescreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat.startActivity
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
        GoogleMapsText(googleMaps = maps.googleMaps)
    }
}

@Composable
private fun ColumnScope.GoogleMapsText(googleMaps: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMaps))
    val context = LocalContext.current
    Text(
        text = "Search in Google Maps: ",
        fontFamily = FontFamily.Cursive,
        color = Color.Blue,
        modifier = Modifier
            .clickable(
                enabled = true,
            ) {
                startActivity(context, intent, null)
            }
            .align(CenterHorizontally)
    )
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