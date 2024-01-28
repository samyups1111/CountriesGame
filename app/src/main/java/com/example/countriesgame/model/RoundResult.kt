package com.example.countriesgame.model

sealed class RoundResult {
    object Player1Won : RoundResult()
    object Player2Won : RoundResult()
    object Draw : RoundResult()
}
