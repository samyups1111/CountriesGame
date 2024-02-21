package com.example.countriesgame.networking

import com.example.countriesgame.model.usecase.SignupResult

interface AuthService {

    suspend fun signup(email: String, password: String): SignupResult

    suspend fun login(email: String, password: String): SignupResult

    suspend fun logOut()
}