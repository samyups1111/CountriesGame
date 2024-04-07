package com.example.countriesgame.model.loginscreen

data class SignupScreenViewData(
    val state: SignupState = SignupState.LOADING,
    val email: String = "",
    val password: String = "",
    val passwordRepeat: String = "",
    val error: String = "",
)

enum class SignupState {
    LOADING,
    IN_PROGRESS,
    SUCCESS,
}