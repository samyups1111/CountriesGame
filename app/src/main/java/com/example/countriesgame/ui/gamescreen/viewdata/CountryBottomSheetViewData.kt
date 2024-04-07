package com.example.countriesgame.ui.gamescreen.viewdata

import com.example.countriesgame.model.CountryMap
import com.example.countriesgame.model.CountryName

data class CountryBottomSheetViewData(
    val countryName: CountryName = CountryName("", ""),
    val capital: List<String> = emptyList(),
    val region: String = "",
    val flag: String = "",
    val maps: CountryMap = CountryMap(googleMaps = "", openStreetMaps = ""),
    val population: Int = 0,
    val unMember: Boolean = false,
    val imgUrl: String = "",
    val languages: String = "",
    val currencies: String = "",
    val borders: String = "",
) {
    fun mock() = CountryBottomSheetViewData(
        countryName = CountryName("United States", "USA"),
        capital = listOf("DC"),
        region = "Americas",
        flag = "",
        maps = CountryMap("", ""),
        population = 100000,
        unMember = true,
        imgUrl = "https://mainfacts.com/media/images/coats_of_arms/ad.svg",
        languages = "English, Spanish",
        borders = "Canada, Mexico",
        currencies = "USD",
    )
}