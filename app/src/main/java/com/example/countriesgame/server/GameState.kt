package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.Player

sealed class GameState {

    object Loading : GameState()

    data class RoundFinished(
        val player1: Player,
        val player2: Player,
        val currentLetter: Char = ' ',
        val missedCountries: List<Country> = emptyList(),
        val numOfMissedCountries: Int = 100,
        val remainingLetters: List<Char> = emptyList(),
        val roundWinner: Player,
    ) : GameState()

    data class GameOver(
        val winner: Player,
    ) : GameState()

    data class RoundInProgress(
        val player1: Player,
        val player2: Player,
        val currentLetter: Char = ' ',
        val countriesRemaining: List<Country> = emptyList(),
        val numOfCountriesLeft: Int = 100,
        val remainingLetters: List<Char> = emptyList(),
        val currentAnswer: String = "",
    ) : GameState()
}
