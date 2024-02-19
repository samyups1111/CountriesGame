package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.Player
import kotlinx.coroutines.flow.StateFlow

interface GameServer {

    val gameState: StateFlow<GameState>

    fun setCountries(countries: List<Country>)
    fun startGame(
        player1: Player,
        player2: Player,
    )
    fun onAnswerSubmitted(answer: String)
    fun onGiveUp()
    fun startNextRound()
}