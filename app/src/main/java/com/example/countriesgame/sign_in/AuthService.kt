package com.example.countriesgame.sign_in

interface AuthService {

    suspend fun signupWithEmailAndPassword(email: String, password: String): SignupResult

    suspend fun loginWithEmailAndPassword(email: String, password: String): SignupResult

    suspend fun logOut()
}