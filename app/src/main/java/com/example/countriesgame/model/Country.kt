package com.example.countriesgame.model

import com.squareup.moshi.Json

/**
 * **See** [API Data Fields](https://gitlab.com/restcountries/restcountries/-/blob/master/FIELDS.md)
 */
data class Country(
    @field:Json(name = "alpha3Code")
    val id: String,
    val name: CountryName,
    val capital: List<String>,
    val region: String,
    val flag: String,
    val maps: CountryMap,
    val population: Int,
    val unMember: Boolean,
    val coatOfArms: CoatOfArms,
)

data class CountryName(
    val common: String,
    val official: String,
)

data class CountryMap(
    val googleMaps: String,
    val openStreetMaps: String,
)

data class CoatOfArms(
    val svg: String,
    val png: String,
)