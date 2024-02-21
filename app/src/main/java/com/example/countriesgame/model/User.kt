package com.example.countriesgame.model

data class User(
    val id: Int,
    val name: String,
    val score: Int,
    val countriesGuessedCorrectly: List<Country>,
    val isItsTurn: Boolean,
    )
