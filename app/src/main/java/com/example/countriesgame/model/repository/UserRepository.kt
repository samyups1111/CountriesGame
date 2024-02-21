package com.example.countriesgame.model.repository

import com.example.countriesgame.model.usecase.SignupResult

interface UserRepository {

    suspend fun signup(email: String, password: String): SignupResult

    suspend fun login(email: String, password: String): SignupResult

    suspend fun signOut()
}