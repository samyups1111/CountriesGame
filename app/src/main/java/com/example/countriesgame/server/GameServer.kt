package com.example.countriesgame.server

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.User
import kotlinx.coroutines.flow.StateFlow

interface GameServer {

    val gameState: StateFlow<GameState>

    fun setCountries(countries: List<Country>)
    fun setPlayers(
        userOne: User,
        userTwo: User,
    )
    fun startGame()
    fun onAnswerSubmitted(answer: String)
    fun onGiveUp()
    fun startNextRound()
}