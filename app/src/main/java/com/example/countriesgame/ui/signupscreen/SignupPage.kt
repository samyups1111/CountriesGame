package com.example.countriesgame.ui.signupscreen

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

@Composable
fun SignupPage(
    state: SignupScreenViewData,
    updateUserNameTextField: (String) -> Unit,
    updatePasswordTextField: (String) -> Unit,
    updatePasswordRepeatTextField: (String) -> Unit,
    goToLoginPage: () -> Unit,
    createNewUser: () -> Unit,
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
            onValueChange = updateUserNameTextField,
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
        TextField(
            value = state.passwordRepeat,
            onValueChange = updatePasswordRepeatTextField,
            placeholder = { Text("re-enter password") },
            modifier = Modifier
                .padding(3.dp)
                .border(
                    BorderStroke(width = 1.dp, color = Color.Black)
                )
        )
        Button(
            onClick = createNewUser,
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
           text = "Already have an account?",
            color = Color.Blue,
            modifier = Modifier
                .padding(3.dp)
                .clickable(
                    enabled = true,
                    onClick = goToLoginPage,
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
fun SignupPagePreview() {
    SignupPage(
        state = SignupScreenViewData(
            email = "email@yahoo.com",
            password = "myPassword4"
        ),
        updateUserNameTextField = {},
        updatePasswordTextField = {},
        updatePasswordRepeatTextField = {},
        goToLoginPage = {},
        createNewUser = {},
        modifier = Modifier
            .fillMaxSize()
    )
}
