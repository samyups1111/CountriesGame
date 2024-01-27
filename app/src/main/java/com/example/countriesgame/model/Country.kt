package com.example.countriesgame.model

import com.squareup.moshi.Json

/**
 * **See** [API Data Fields](https://gitlab.com/restcountries/restcountries/-/blob/master/FIELDS.md)
 */
data class Country(
    val id: String,
    val name: CountryName,
    val capital: List<String>,
    val region: String,
    val flag: String,
    val maps: CountryMap,
    val population: Int,
    val unMember: Boolean,
    val coatOfArms: CoatOfArms,
    val languages: List<String>,
    val borders: List<String>,
    val currencies: List<String>,
)

data class CountryRemote(
    @field:Json(name = "cca3")
    val id: String,
    val name: CountryName,
    val capital: List<String>,
    val region: String,
    val flag: String,
    val maps: CountryMap,
    val population: Int,
    val unMember: Boolean,
    val coatOfArms: CoatOfArms,
    val languages: Map<String, String>,
    val borders: List<String>,
    val currencies: Map<String, Currency>,
)

data class Currency(
    val name: String,
    val symbol: String,
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

fun CountryRemote.toCountry(): Country {

    val languages = mutableListOf<String>()
    val currencies = mutableListOf<String>()
    this.languages.values.forEach { language ->
        languages.add(language)
    }
    this.currencies.values.forEach {  currency ->
        currencies.add(currency.symbol + " " + currency.name)
    }

    return Country(
        id = this.id,
        name = this.name,
        capital = this.capital,
        coatOfArms = this.coatOfArms,
        flag = this.flag,
        languages = languages,
        maps = this.maps,
        population = this.population,
        region = this.region,
        unMember = this.unMember,
        currencies = currencies,
        borders = this.borders,
    )
}