package com.example.countriesgame.ui.signupscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesgame.sign_in.SignupResult
import com.example.countriesgame.sign_in.SignupWithEmailAndPasswordUseCase
import com.example.countriesgame.sign_in.VerifySignupInputFormatUseCase
import com.example.countriesgame.model.loginscreen.SignupScreenViewData
import com.example.countriesgame.model.loginscreen.SignupState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupScreenViewModel @Inject constructor(
    private val verifySignupInputFormatUseCase: VerifySignupInputFormatUseCase,
    private val signupWithEmailAndPasswordUseCase: SignupWithEmailAndPasswordUseCase,
) : ViewModel() {

    var uiState by mutableStateOf(SignupScreenViewData())
        private set

    fun signup() {
        viewModelScope.launch {
            when (val formatResult = verifySignupFormatInput()) {
                is SignupResult.Success -> { createNewUser() }
                is SignupResult.Error -> {
                    uiState = uiState.copy(error = formatResult.e)
                }
            }
        }
    }

    private fun verifySignupFormatInput(): SignupResult {
        return verifySignupInputFormatUseCase.invoke(
            email = uiState.email,
            password = uiState.password,
            passwordRepeat = uiState.passwordRepeat,
        )
    }

    private suspend fun createNewUser() {
        val signupResult = signupWithEmailAndPasswordUseCase.invoke(uiState.email, uiState.password)
        uiState = uiState.copy(
            state = when (signupResult) {
                is SignupResult.Success -> {
                    SignupState.SUCCESS
                }
                is SignupResult.Error -> {
                    SignupState.IN_PROGRESS
                }
            },
            error = if (signupResult is SignupResult.Error) signupResult.e else ""
        )
    }

    fun updateEmailTextField(name: String) {
        uiState = uiState.copy(email = name, error = "")
    }

    fun updatePasswordTextField(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun updatePasswordRepeatTextField(password: String) {
        uiState = uiState.copy(passwordRepeat = password)
    }
}