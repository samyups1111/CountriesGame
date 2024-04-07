package com.example.countriesgame.ui.gamescreen.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.countriesgame.ui.gamescreen.viewdata.CountryBottomSheetViewData
import com.example.countriesgame.ui.theme.CountriesGameTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CountryBottomSheet(
    data: CountryBottomSheetViewData,
    hideBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val officialName = data.countryName.official
    val commonName = data.countryName.common
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        modifier = modifier
    ) {
        Text(
            text = data.flag + officialName,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .align(CenterHorizontally)
        )
        if (commonName != officialName) {
            Text(
                text = commonName,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(CenterHorizontally)
            )
        }
        GlideImage(
            model = data.imgUrl,
            contentDescription = "${officialName}'s Image",
            modifier = Modifier
                .size(300.dp)
                .align(CenterHorizontally)
        )
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "Capital: ")
            Text(text = data.capital.toString())
        }
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "Located in: ")
            Text(text = data.region)
        }
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "Population: ")
            Text(text = data.population.toString())
        }
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "United Nations Member?: ")
            Text(text = data.unMember.toString())
        }
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "Languages: ")
            Text(text = data.languages)
        }
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "Borders: ")
            Text(text = data.borders)
        }
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "Currencies: ")
            Text(text = data.currencies)
        }
        GoogleMapsText(googleMaps = data.maps.googleMaps, modifier = Modifier.padding(top = 20.dp, bottom = 10.dp))
        WikiLink(name = officialName, modifier = Modifier.padding(bottom = 60.dp))
    }
}

@Composable
private fun ColumnScope.GoogleMapsText(
    googleMaps: String,
    modifier: Modifier = Modifier,
) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMaps))
    val context = LocalContext.current
    Text(
        text = "Search in Google Maps: ",
        fontFamily = FontFamily.Cursive,
        color = Color.Blue,
        modifier = modifier
            .clickable(
                enabled = true,
            ) {
                startActivity(context, intent, null)
            }
            .align(CenterHorizontally)
    )
}

@Composable
private fun ColumnScope.WikiLink(
    name: String,
    modifier: Modifier = Modifier,
) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/$name"))
    val context = LocalContext.current

    Text(
        text = "Search in Wikipedia: ",
        fontFamily = FontFamily.Cursive,
        color = Color.Red,
        modifier = modifier
            .clickable(
                enabled = true,
            ) {
                startActivity(context, intent, null)
            }
            .align(CenterHorizontally)
    )
}

@Preview
@Composable
fun CountryBottomSheetPreview() {
    CountriesGameTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            CountryBottomSheet(
                data = CountryBottomSheetViewData().mock(),
                hideBottomSheet = {},
            )
        }
    }
}