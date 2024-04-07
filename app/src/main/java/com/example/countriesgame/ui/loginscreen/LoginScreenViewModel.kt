package com.example.countriesgame.ui.loginscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.sign_in.LoginWithEmailAndPasswordUseCase
import com.example.countriesgame.sign_in.SignupResult
import com.example.countriesgame.sign_in.VerifySignupInputFormatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val verifySignupInputFormatUseCase: VerifySignupInputFormatUseCase,
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase,
    ) : ViewModel() {

    var uiState by mutableStateOf(LoginScreenViewData())
        private set

    fun verifyInputAndLogin() {
        viewModelScope.launch {
            val formatResult = verifySignupInputFormatUseCase.invoke(
                email = uiState.email,
                password = uiState.password,
            )

            when (formatResult) {
                is SignupResult.Success -> { loginToRemote() }
                is SignupResult.Error -> {
                    uiState = uiState.copy(error = formatResult.e)
                }
            }
        }
    }

    private fun loginToRemote() {
        viewModelScope.launch {
            val loginResult = loginWithEmailAndPasswordUseCase.invoke(
                email = uiState.email,
                password = uiState.password,
            )

            uiState = uiState.copy(
                state = when (loginResult) {
                    is SignupResult.Success -> {
                        LoginState.SUCCESS
                    }

                    is SignupResult.Error -> {
                        LoginState.IN_PROGRESS
                    }
                },
                error = if (loginResult is SignupResult.Error) loginResult.e else ""
            )
        }
    }

    fun updateEmailTextField(name: String) {
        uiState = uiState.copy(email = name, error = "")
    }

    fun updatePasswordTextField(password: String) {
        uiState = uiState.copy(password = password)
    }
}