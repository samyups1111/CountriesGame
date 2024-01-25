package com.example.countriesgame.di

import com.example.countriesgame.model.CountryRepository
import com.example.countriesgame.model.CountryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNasaImageRepositoryImpl(
        repository: CountryRepositoryImpl,
    ): CountryRepository
}