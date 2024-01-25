package com.example.countriesgame.ui.gamescreen

sealed class BottomSheetState {
    object Hide : BottomSheetState()

    data class Show(
        val countryName: String,
        val imgUrl: String?,
        val description: String,
    ) : BottomSheetState()
}
