package com.example.countriesgame.ui.loginscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.model.usecase.LoginUseCase
import com.example.countriesgame.model.usecase.SignupResult
import com.example.countriesgame.model.usecase.VerifySignupInputFormatUseCase
import com.example.countriesgame.ui.loginscreen.state.LoginScreenUiState
import com.example.countriesgame.ui.signupscreen.state.SignupScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val verifySignupInputFormatUseCase: VerifySignupInputFormatUseCase,
    private val loginUseCase: LoginUseCase,
    ) : ViewModel() {

    var uiState by mutableStateOf<LoginScreenUiState>(LoginScreenUiState.InProgress())
        private set

    fun verifyInputAndLogin() {
        viewModelScope.launch {
            val state = uiState as LoginScreenUiState.InProgress
            val formatResult = verifySignupInputFormatUseCase.invoke(
                email = state.email,
                password = state.password,
            )

            when (formatResult) {
                is SignupResult.Success -> { loginToRemote() }
                is SignupResult.Error -> {
                    uiState = state.copy(error = formatResult.e)
                }
            }
        }
    }

    private fun loginToRemote() {
        viewModelScope.launch {
            val state = uiState as LoginScreenUiState.InProgress
            val loginResult = loginUseCase.invoke(email = state.email, password = state.password)

            uiState = when (loginResult) {
                is SignupResult.Success -> {
                    LoginScreenUiState.Success
                }

                is SignupResult.Error -> {
                    LoginScreenUiState.InProgress(error = loginResult.e)
                }
            }
        }
    }

    fun updateEmailTextField(name: String) {
        val state = uiState as LoginScreenUiState.InProgress
        uiState = state.copy(email = name, error = "")
    }

    fun updatePasswordTextField(password: String) {
        val state = uiState as LoginScreenUiState.InProgress
        uiState = state.copy(password = password)
    }
}