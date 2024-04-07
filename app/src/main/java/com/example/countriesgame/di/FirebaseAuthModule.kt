package com.example.countriesgame.di

import com.example.countriesgame.sign_in.AuthService
import com.example.countriesgame.sign_in.FirebaseAuthClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseAuthServiceModule {

    @Binds
    @Singleton
    abstract fun bindFirebaseAuthService(
        auth: FirebaseAuthClient,
    ): AuthService
}