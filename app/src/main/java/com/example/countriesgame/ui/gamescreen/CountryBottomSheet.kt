package com.example.countriesgame.ui.gamescreen

import android.widget.TextView
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.countriesgame.ui.theme.CountriesGameTheme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CountryBottomSheet(
    countryName: String,
    imgUrl: String?,
    description: String,
    modifier: Modifier = Modifier,
    hideBottomSheet: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = hideBottomSheet,
        modifier = modifier
    ) {
        Text(
            text = countryName,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .align(CenterHorizontally)
        )
        GlideImage(
            model = imgUrl,
            contentDescription = "Country Image",
            modifier = Modifier
                .size(300.dp)
                .align(CenterHorizontally)
        )
        Html(text = description)
    }
}

@Composable
fun Html(
    text: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
            }
        },
        modifier = modifier
            .padding(5.dp)
            .verticalScroll(
                state = ScrollState(0),
                enabled = true,
            )
    )
}

@Preview
@Composable
fun CountryBottomSheetPreview() {
    CountriesGameTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CountryBottomSheet(
                countryName = "United States",
                imgUrl = null,
                description = "The United States of America (USA or U.S.A.), commonly known as the United States (US or U.S.) or America, is a country primarily located in North America. The third-largest country in the world by land and total area,[c] the U.S. consists of 50 states, a federal district, five major unincorporated territories, nine Minor Outlying Islands[i] and includes 326 Indian reservations. It shares land borders with Canada to its north and with Mexico to its south and has maritime borders with several other countries.[j] With a population of over 334 million,[k] it is the most populous country in the Americas and the third-most populous in the world. The national capital of the United States is Washington, D.C., and its most populous city and principal financial center is New York City."
            )
        }
    }
}