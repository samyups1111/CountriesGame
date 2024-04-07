package com.example.countriesgame.sign_in

import javax.inject.Inject

class LoginWithEmailAndPasswordUseCase @Inject constructor(
    private val auth: AuthService,
) {
    suspend fun invoke(email: String, password: String): SignupResult {
        return auth.loginWithEmailAndPassword(email, password)
    }
}