package com.example.countriesgame.sign_in

import javax.inject.Inject

class SignupWithEmailAndPasswordUseCase @Inject constructor(
    private val auth: AuthService,
) {
    suspend fun invoke(
        email: String,
        password: String,
    ): SignupResult {
        return auth.signupWithEmailAndPassword(email, password)
    }
}