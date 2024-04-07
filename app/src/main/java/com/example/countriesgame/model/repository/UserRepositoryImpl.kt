package com.example.countriesgame.model.repository

import com.example.countriesgame.sign_in.SignupResult
import com.example.countriesgame.sign_in.AuthService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: AuthService,
) : UserRepository {

    override suspend fun signupWithEmailAndPassword(email: String, password: String): SignupResult {
        return auth.signupWithEmailAndPassword(email, password)
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String): SignupResult {
        return auth.loginWithEmailAndPassword(email, password)
    }

    override suspend fun signOut() {
        auth.logOut()
    }
}