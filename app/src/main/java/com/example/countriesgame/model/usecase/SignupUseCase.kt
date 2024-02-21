package com.example.countriesgame.model.usecase

import com.example.countriesgame.networking.AuthService
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val auth: AuthService,
) {
    suspend fun invoke(
        email: String,
        password: String,
    ): SignupResult {
        return auth.signupWithEmailAndPassword(email, password)
    }
}