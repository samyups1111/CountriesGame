package com.example.countriesgame.model.repository

import com.example.countriesgame.model.usecase.SignupResult

interface UserRepository {

    suspend fun signupWithEmailAndPassword(email: String, password: String): SignupResult

    suspend fun loginWithEmailAndPassword(email: String, password: String): SignupResult

    suspend fun signOut()
}