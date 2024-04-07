package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.User
import com.example.countriesgame.model.gamescreen.GameStateLabel


data class GameState(
    val gameStateLabel: GameStateLabel = GameStateLabel.LOADING,
    val user1: User = User(),
    val user2: User = User(),
    val currentLetter: Char = ' ',
    val countriesRemaining: List<Country> = emptyList(),
    val numOfCountriesLeft: Int = 100,
    val remainingLetters: List<Char> = emptyList(),
    val currentAnswer: String = "",
    val gameWinner: User = User(),
)