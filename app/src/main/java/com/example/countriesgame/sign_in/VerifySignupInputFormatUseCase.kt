package com.example.countriesgame.sign_in

import javax.inject.Inject

class VerifySignupInputFormatUseCase @Inject constructor() {

    private lateinit var _email: String
    private lateinit var _password: String
    private var _passwordRepeat: String? = null

    fun invoke(
        email: String,
        password: String,
        passwordRepeat: String? = null,
    ): SignupResult {
        _email = email
        _password = password
        _passwordRepeat = passwordRepeat

        val emailResult = verifyEmail()
        if (emailResult is SignupResult.Error) return emailResult

        val passwordResult = verifyPassword()
        if (passwordResult is SignupResult.Error) return passwordResult

        if (_passwordRepeat != null) {
            val passwordRepeatResult = verifyPasswordRepeat()
            if (passwordRepeatResult is SignupResult.Error) return passwordRepeatResult
        }
        return SignupResult.Success
    }

    private fun verifyEmail(): SignupResult {
        if (!_email.contains('@')) return SignupResult.Error("email doesn't have '@'")
        if (!_email.contains('.')) return SignupResult.Error("email doesn't have a '.'")

        return SignupResult.Success
    }

    private fun verifyPassword(): SignupResult {
        if (!passwordContainsProperCharacters()) return SignupResult.Error("password must contain letters and digits")
        if (!passwordHasCorrectLength()) return SignupResult.Error("Password must have 6-24 characters")
        return SignupResult.Success
    }

    private fun passwordContainsProperCharacters(): Boolean {
        if (!_password.any { it.isDigit() }) return false
        if (!_password.any { it.isLetter() }) return false
        return true
    }

    private fun passwordHasCorrectLength(): Boolean {
        if (_password.length < 5 || _password.length > 25) return false
        return true
    }

    private fun verifyPasswordRepeat(): SignupResult {
        return if (_password == _passwordRepeat) SignupResult.Success
        else SignupResult.Error("Passwords do not match")
    }
}

sealed class SignupResult {
    object Success : SignupResult()

    data class Error(val e: String) : SignupResult()
}