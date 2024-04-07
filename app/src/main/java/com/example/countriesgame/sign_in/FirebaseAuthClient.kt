package com.example.countriesgame.sign_in

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthClient @Inject constructor(
    private val auth: FirebaseAuth,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): AuthService {

    override suspend fun signupWithEmailAndPassword(
        email: String,
        password: String,
    ): SignupResult = withContext(ioDispatcher) {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user: FirebaseUser? = result.user

            if (user != null) SignupResult.Success
            else SignupResult.Error("Cant access user")
        } catch (e: Exception) {
            SignupResult.Error(e.message.toString())
        }
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String): SignupResult = withContext(ioDispatcher) {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user: FirebaseUser? = result.user

            if (user != null) SignupResult.Success
            else SignupResult.Error("error: Couldn't login")
        } catch (e: Exception) {
            SignupResult.Error(e.message.toString())
        }
    }

    override suspend fun logOut() = withContext(ioDispatcher) { auth.signOut() }
}