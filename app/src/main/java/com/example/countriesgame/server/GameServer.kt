package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.Player
import kotlinx.coroutines.flow.StateFlow

interface GameServer {

    val gameState: StateFlow<GameState>

    fun setCountries(countries: List<Country>)
    fun setPlayers(
        playerOne: Player,
        playerTwo: Player,
    )
    fun startGame()
    fun onAnswerSubmitted(answer: String)
    fun onGiveUp()
    fun startNextRound()
}