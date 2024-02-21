package com.example.countriesgame.networking

import com.example.countriesgame.model.usecase.SignupResult

interface AuthService {

    suspend fun signupWithEmailAndPassword(email: String, password: String): SignupResult

    suspend fun loginWithEmailAndPassword(email: String, password: String): SignupResult

    suspend fun logOut()
}