package com.example.countriesgame.model.loginscreen

data class LoginScreenViewData(
    val state: LoginState = LoginState.LOADING,
    val email: String = "",
    val password: String = "",
    val error: String = "",
)

enum class LoginState {
    LOADING,
    IN_PROGRESS,
    SUCCESS,
}