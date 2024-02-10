package com.example.countriesgame.model

data class Player(
    val id: Int,
    val name: String,
    val score: Int,
    val countriesGuessedCorrectly: List<Country>,
    val isItsTurn: Boolean,
    )
