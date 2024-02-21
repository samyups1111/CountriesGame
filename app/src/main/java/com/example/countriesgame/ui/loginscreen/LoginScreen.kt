package com.example.countriesgame.ui.loginscreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.countriesgame.ui.loginscreen.page.LoginPage
import com.example.countriesgame.ui.loginscreen.state.LoginScreenUiState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    goToHome: () -> Unit,
    goToSignupScreen: () -> Unit,
    vm: LoginScreenViewModel = hiltViewModel()
) {
    when (val state = vm.uiState) {
        is LoginScreenUiState.InProgress -> {
            LoginPage(
                state = state,
                updateEmailTextField = { email: String -> vm.updateEmailTextField(email) },
                updatePasswordTextField = { password: String -> vm.updatePasswordTextField(password) },
                login = { vm.verifyInputAndLogin() },
                goToSignupPage = goToSignupScreen,
                modifier = modifier,
            )
        }
        is LoginScreenUiState.Loading -> {
           Text(text = "Loading...")
        }
        is LoginScreenUiState.Success -> { goToHome() }
    }
}