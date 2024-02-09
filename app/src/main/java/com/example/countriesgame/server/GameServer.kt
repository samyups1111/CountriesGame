package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import kotlinx.coroutines.flow.StateFlow

interface GameServer {
    val gameState: StateFlow<GameState>
    val allCountries: List<Country>

    fun setCountries(countries: List<Country>)
    fun startGame()
    fun onAnswerSubmitted(answer: String)
    fun onGiveUp()
    fun startNextRound()
}