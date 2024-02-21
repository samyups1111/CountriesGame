package com.example.countriesgame.model.repository

import com.example.countriesgame.model.usecase.SignupResult
import com.example.countriesgame.networking.AuthService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: AuthService,
) : UserRepository {

    override suspend fun signup(email: String, password: String): SignupResult {
        return auth.signup(email, password)
    }

    override suspend fun login(email: String, password: String): SignupResult {
        return auth.login(email, password)
    }

    override suspend fun signOut() {
        auth.logOut()
    }
}