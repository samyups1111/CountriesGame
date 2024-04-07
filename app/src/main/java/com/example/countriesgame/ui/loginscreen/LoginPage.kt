package com.example.countriesgame.ui.loginscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countriesgame.model.loginscreen.LoginScreenViewData

@Composable
fun LoginPage(
    state: LoginScreenViewData,
    updateEmailTextField: (String) -> Unit,
    updatePasswordTextField: (String) -> Unit,
    goToSignupPage: () -> Unit,
    login: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(5.dp),
    ) {
        TextField(
            value = state.email,
            onValueChange = updateEmailTextField,
            placeholder = { Text("email") },
            modifier = Modifier
                .padding(3.dp)
                .border(
                    BorderStroke(width = 1.dp, color = Color.Black)
                )
        )
        TextField(
            value = state.password,
            onValueChange = updatePasswordTextField,
            placeholder = { Text("password") },
            modifier = Modifier
                .padding(3.dp)
                .border(
                    BorderStroke(width = 1.dp, color = Color.Black)
                )
        )
        Button(
            onClick = login,
            modifier = Modifier
                .padding(3.dp)
        ) {
            Text(
                text = "Submit",
                modifier = Modifier
                    .padding(3.dp)
            )
        }
        Text(
            text = "Don't have an account?",
            color = Color.Blue,
            modifier = Modifier
                .padding(3.dp)
                .clickable(
                    enabled = true,
                    onClick = goToSignupPage,
                )
        )
        if (state.error != "") {
            Text(
                text = state.error,
                modifier = Modifier
                    .padding(3.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    LoginPage(
        state = LoginScreenViewData(
            email = "sdejesus@yahoo.com",
            password = "12345"
        ),
        updateEmailTextField = {},
        updatePasswordTextField = {},
        goToSignupPage = {},
        login = {},
        modifier = Modifier.fillMaxSize()
    )
}