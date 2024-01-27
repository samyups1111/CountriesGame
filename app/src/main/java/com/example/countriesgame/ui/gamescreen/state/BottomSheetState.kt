package com.example.countriesgame.ui.gamescreen.state

import com.example.countriesgame.model.CountryMap
import com.example.countriesgame.model.CountryName

sealed class BottomSheetState {
    object Hide : BottomSheetState()

    data class Show(
        val countryName: CountryName,
        val capital: List<String>,
        val region: String,
        val flag: String,
        val maps: CountryMap,
        val population: Int,
        val unMember: Boolean,
        val imgUrl: String,
        val languages: String,
        val currencies: String,
        val borders: String,
    ) : BottomSheetState()
}
