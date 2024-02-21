package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.User

sealed class GameState {

    object Loading : GameState()

    data class RoundFinished(
        val user1: User,
        val user2: User,
        val currentLetter: Char = ' ',
        val missedCountries: List<Country> = emptyList(),
        val numOfMissedCountries: Int = 100,
        val remainingLetters: List<Char> = emptyList(),
        val roundWinner: User,
    ) : GameState()

    data class GameOver(
        val winner: User,
    ) : GameState()

    data class RoundInProgress(
        val user1: User,
        val user2: User,
        val currentLetter: Char = ' ',
        val countriesRemaining: List<Country> = emptyList(),
        val numOfCountriesLeft: Int = 100,
        val remainingLetters: List<Char> = emptyList(),
        val currentAnswer: String = "",
    ) : GameState()
}
