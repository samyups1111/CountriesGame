package com.example.countriesgame.model.usecase

import com.example.countriesgame.model.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun invoke(email: String, password: String): SignupResult {
        return userRepository.login(email, password)
    }
}