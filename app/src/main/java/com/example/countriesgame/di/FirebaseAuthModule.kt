package com.example.countriesgame.di

import com.example.countriesgame.networking.AuthService
import com.example.countriesgame.networking.FirebaseAuthService
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
        auth: FirebaseAuthService,
    ): AuthService
}