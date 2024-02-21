package com.example.countriesgame.ui.signupscreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.countriesgame.ui.signupscreen.page.SignupPage
import com.example.countriesgame.ui.signupscreen.state.SignupScreenUiState

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    goToHomeScreen: () -> Unit,
    goToLoginScreen: () -> Unit,
    vm: SignupScreenViewModel = hiltViewModel(),
) {
    when (val state = vm.uiState) {
        is SignupScreenUiState.InProgress -> {
            SignupPage(
                state = state,
                modifier = modifier,
                updateUserNameTextField = { name: String -> vm.updateEmailTextField(name) },
                updatePasswordTextField = { password: String -> vm.updatePasswordTextField(password) },
                updatePasswordRepeatTextField = { password: String -> vm.updatePasswordRepeatTextField(password) },
                goToLoginPage = goToLoginScreen,
                createNewUser = { vm.signup() },
            )
        }

        is SignupScreenUiState.Loading -> {
            Text("Loading...")
        }

        is SignupScreenUiState.Success -> { goToHomeScreen() }
    }
}