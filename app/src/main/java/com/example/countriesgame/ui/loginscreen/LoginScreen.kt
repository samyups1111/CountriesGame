package com.example.countriesgame.ui.loginscreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.countriesgame.model.loginscreen.LoginState

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    goToHome: () -> Unit,
    goToSignupScreen: () -> Unit,
    vm: LoginScreenViewModel = hiltViewModel()
) {
    when (vm.uiState.state) {
        LoginState.IN_PROGRESS -> {
            LoginPage(
                state = vm.uiState,
                updateEmailTextField = { email: String -> vm.updateEmailTextField(email) },
                updatePasswordTextField = { password: String -> vm.updatePasswordTextField(password) },
                login = { vm.verifyInputAndLogin() },
                goToSignupPage = goToSignupScreen,
                modifier = modifier,
            )
        }
        LoginState.LOADING -> {
           Text(text = "Loading...")
        }
        LoginState.SUCCESS -> { goToHome() }
    }
}