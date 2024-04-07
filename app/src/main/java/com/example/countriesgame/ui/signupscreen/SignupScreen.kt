package com.example.countriesgame.ui.signupscreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    goToHomeScreen: () -> Unit,
    goToLoginScreen: () -> Unit,
    vm: SignupScreenViewModel = hiltViewModel(),
) {
    when (val state = vm.uiState.state) {
        SignupState.IN_PROGRESS -> {
            SignupPage(
                state = vm.uiState,
                modifier = modifier,
                updateUserNameTextField = { name: String -> vm.updateEmailTextField(name) },
                updatePasswordTextField = { password: String -> vm.updatePasswordTextField(password) },
                updatePasswordRepeatTextField = { password: String -> vm.updatePasswordRepeatTextField(password) },
                goToLoginPage = goToLoginScreen,
                createNewUser = { vm.signup() },
            )
        }

        SignupState.LOADING -> {
            Text("Loading...")
        }

        SignupState.SUCCESS -> { goToHomeScreen() }
    }
}