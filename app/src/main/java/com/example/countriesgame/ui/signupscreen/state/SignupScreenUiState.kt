package com.example.countriesgame.ui.signupscreen.state

sealed class SignupScreenUiState {

    object Loading : SignupScreenUiState()

    data class InProgress(
        val email: String = "",
        val password: String = "",
        val passwordRepeat: String = "",
        val error: String = "",
    ) : SignupScreenUiState()

    object Success : SignupScreenUiState()
}