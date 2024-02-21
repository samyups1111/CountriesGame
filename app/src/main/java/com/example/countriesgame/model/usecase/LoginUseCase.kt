package com.example.countriesgame.model.usecase

import com.example.countriesgame.networking.AuthService
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val auth: AuthService,
) {
    suspend fun invoke(email: String, password: String): SignupResult {
        return auth.loginWithEmailAndPassword(email, password)
    }
}