package com.example.countriesgame.di

import com.example.countriesgame.model.repository.CountryRepository
import com.example.countriesgame.model.repository.CountryRepositoryImpl
import com.example.countriesgame.model.repository.UserRepository
import com.example.countriesgame.model.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCountryRepositoryImpl(
        repository: CountryRepositoryImpl,
    ): CountryRepository

    @Binds
    abstract fun bindUserRepositoryImpl(
        repository: UserRepositoryImpl,
    ): UserRepository
}