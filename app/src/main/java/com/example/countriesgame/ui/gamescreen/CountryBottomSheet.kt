package com.example.countriesgame.ui.gamescreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.countriesgame.model.CountryMap
import com.example.countriesgame.ui.theme.CountriesGameTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
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
    imgUrl: String,
    modifier: Modifier = Modifier,
    hideBottomSheet: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        modifier = modifier
    ) {
        Text(
            text = flag + officialName,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(CenterHorizontally)
        )
        Text(
            text = commonName,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(CenterHorizontally)
        )
        GlideImage(
            model = imgUrl,
            contentDescription = "$officialName's Image",
            modifier = Modifier
                .size(300.dp)
                .align(CenterHorizontally)
        )
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "Capital: ")
            Text(text = capital)
        }
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "Located in: ")
            Text(text = region)
        }
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "Population: ")
            Text(text = population.toString())
        }
        Row(modifier = Modifier.padding(10.dp)) {
            Text(text = "United Nations Member?: ")
            Text(text = unMember.toString())
        }
        GoogleMapsText(googleMaps = maps.googleMaps, modifier = Modifier.padding(40.dp))
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

@Preview
@Composable
fun CountryBottomSheetPreview() {
    CountriesGameTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            CountryBottomSheet(
                officialName = "United States",
                commonName = "USA",
                capital = "DC",
                region = "Americas",
                flag = "",
                maps = CountryMap("", ""),
                population = 100000,
                unMember = true,
                imgUrl = "https://mainfacts.com/media/images/coats_of_arms/ad.svg",
            )
        }
    }
}