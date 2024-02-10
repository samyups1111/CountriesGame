package com.example.countriesgame.ui.gamescreen.state

sealed class SearchBarState {

    object Empty : SearchBarState()

    data class HasQuery(val text: String) : SearchBarState()
}