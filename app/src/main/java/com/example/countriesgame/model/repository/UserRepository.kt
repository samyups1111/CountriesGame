package com.example.countriesgame.model.repository

import com.example.countriesgame.sign_in.SignupResult

interface UserRepository {

    suspend fun signupWithEmailAndPassword(email: String, password: String): SignupResult

    suspend fun loginWithEmailAndPassword(email: String, password: String): SignupResult

    suspend fun signOut()
}