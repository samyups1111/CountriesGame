package com.example.countriesgame.model

import com.squareup.moshi.Json

data class Country(
    val name: String,
    val altNames: List<String>? = null,
    val imgUrl: String? = null,
    val description: String,
)

data class WikiResponseObject(
    val parse: WikiQueryObject,
)

data class WikiQueryObject(
    val title: String,
    val pageId: String,
    val text: WikiTextObject,
)

data class WikiTextObject(
    @field:Json(name = "*")
    val description: String,
)

fun String.toCountry(): Country {

    val altNames = when (this) {
        "Bosnia and Herzegovina" -> listOf("Bosnia")
        "Central African Republic" -> listOf("CAR")
        "Saint Kitts and Nevis" -> listOf("Saint Kitts")
        "Saint Vincent and the Grenadines" -> listOf("Saint Vincent")
        "Sao Tome and Principe" -> listOf("Sao Tome")
        "Trinidad and Tobago" -> listOf("Trinidad")
        "United Arab Emirates" -> listOf("UAE")
        "United Kingdom" -> listOf("UK")
        "United States" -> listOf("USA")
        else -> null
    }

    return Country(
        name = this,
        altNames = altNames,
        imgUrl = null,
        description = "",
    )
}
