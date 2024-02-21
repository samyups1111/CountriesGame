package com.example.countriesgame.ui.loginscreen.state

sealed class LoginScreenUiState {

    data class InProgress(
        val email: String = "",
        val password: String = "",
        val error: String = "",
    ) : LoginScreenUiState()

    object Loading : LoginScreenUiState()

    object Success : LoginScreenUiState()
}