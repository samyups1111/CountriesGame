package com.example.countriesgame.ui.loginscreen.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.countriesgame.ui.loginscreen.state.LoginScreenUiState

@Composable
fun LoginPage(
    state: LoginScreenUiState.InProgress,
    updateEmailTextField: (String) -> Unit,
    updatePasswordTextField: (String) -> Unit,
    goToSignupPage: () -> Unit,
    login: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier
            .padding(5.dp),
    ) {
        TextField(
            value = state.email,
            onValueChange = updateEmailTextField,
            placeholder = { Text("email") },
            modifier = Modifier
                .border(
                    BorderStroke(width = 1.dp, color = Color.Black)
                )
        )
        TextField(
            value = state.password,
            onValueChange = updatePasswordTextField,
            placeholder = { Text("password") },
            modifier = Modifier
                .border(
                    BorderStroke(width = 1.dp, color = Color.Black)
                )
        )
        Button(
            onClick = login,
        ) {
            Text(text = "Submit")
        }
        Text(
            text = "Don't have an account?",
            color = Color.Blue,
            modifier = Modifier
                .clickable(
                    enabled = true,
                    onClick = goToSignupPage,
                )
        )
        if (state.error != "") {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = state.error)
        }
    }
}