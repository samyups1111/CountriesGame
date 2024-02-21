package com.example.countriesgame.ui.signupscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.model.usecase.SignupResult
import com.example.countriesgame.model.usecase.SignupUseCase
import com.example.countriesgame.model.usecase.VerifySignupInputFormatUseCase
import com.example.countriesgame.ui.signupscreen.state.SignupScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupScreenViewModel @Inject constructor(
    private val verifySignupInputFormatUseCase: VerifySignupInputFormatUseCase,
    private val signupUseCase: SignupUseCase,
) : ViewModel() {

    var uiState by mutableStateOf<SignupScreenUiState>(SignupScreenUiState.InProgress())
        private set

    fun signup() {
        viewModelScope.launch {
            when (val formatResult = verifySignupFormatInput()) {
                is SignupResult.Success -> { createNewUser() }
                is SignupResult.Error -> {
                    uiState = (uiState as SignupScreenUiState.InProgress).copy(error = formatResult.e)
                }
            }
        }
    }

    private fun verifySignupFormatInput(): SignupResult {
        val state = uiState as SignupScreenUiState.InProgress
        return verifySignupInputFormatUseCase.invoke(
            email = state.email,
            password = state.password,
            passwordRepeat = state.passwordRepeat,
        )
    }

    private suspend fun createNewUser() {
        val state = uiState as SignupScreenUiState.InProgress
        val signupResult = signupUseCase.invoke(state.email, state.password)
        when (signupResult) {
            is SignupResult.Success -> {
                uiState = SignupScreenUiState.Success
            }
            is SignupResult.Error -> {
                uiState = (uiState as SignupScreenUiState.InProgress).copy(error = signupResult.e)
            }
        }
    }

    fun updateEmailTextField(name: String) {
        val state = uiState as SignupScreenUiState.InProgress
        uiState = state.copy(email = name, error = "")
    }

    fun updatePasswordTextField(password: String) {
        val state = uiState as SignupScreenUiState.InProgress
        uiState = state.copy(password = password)
    }

    fun updatePasswordRepeatTextField(password: String) {
        val state = uiState as SignupScreenUiState.InProgress
        uiState = state.copy(passwordRepeat = password)
    }
}