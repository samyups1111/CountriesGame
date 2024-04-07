package com.example.countriesgame.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.countriesgame.model.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val auth: FirebaseAuth,
) {

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.Builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("40089367109-ri41l08crqj9pg71jrpdg04p4oan86l9.apps.googleusercontent.com")
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    suspend fun signInWithIntent(intent: Intent): SignupResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredential).await().user
            SignupResult.Success
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignupResult.Error(e = e.message.toString())
        }
    }

    fun getSignedInUser(): User? = auth.currentUser?.run {
        User(
            id = this.uid,
            name = this.displayName ?: "",
            email = this.email ?: "",
            score = 0,
            countriesGuessedCorrectly = emptyList(),
            isItsTurn = false,
        )
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) {

            }
        }
    }
}