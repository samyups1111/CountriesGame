package com.example.countriesgame.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val score: Int = 0,
    val countriesGuessedCorrectly: List<Country> = emptyList(),
    val isItsTurn: Boolean = false,
    val isRoundWinner: Boolean = false,
    val isGameWinner: Boolean = false,
    )
