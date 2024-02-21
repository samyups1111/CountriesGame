package com.example.countriesgame.model.repository

import com.example.countriesgame.model.usecase.SignupResult

interface UserRepository {

    suspend fun signupNewUser(email: String, password: String): SignupResult

    suspend fun login(email: String, password: String): SignupResult

    suspend fun signOut()

    suspend fun deleteUser()
}