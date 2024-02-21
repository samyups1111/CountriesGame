package com.example.countriesgame.model.repository

import com.example.countriesgame.model.usecase.SignupResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {

    override suspend fun signupNewUser(
        email: String,
        password: String,
    ): SignupResult = withContext(ioDispatcher) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
            SignupResult.Success
        } catch(e: Exception) {
            SignupResult.Error(e.message ?: "error")
        }
    }

    override suspend fun login(email: String, password: String): SignupResult = withContext(ioDispatcher) {
        try {
            auth.signInWithEmailAndPassword(email, password)
            SignupResult.Success
        } catch (e: Exception) {
            SignupResult.Error(e.message.toString())
        }
    }

    override suspend fun signOut() = withContext(ioDispatcher) { auth.signOut() }

    override suspend fun deleteUser() {}
}