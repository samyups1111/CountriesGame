package com.example.countriesgame.di

import com.example.countriesgame.networking.CountriesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideRestCountriesService(
        retrofit: Retrofit,
    ): CountriesService = retrofit.create(CountriesService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(
        moshi: MoshiConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://restcountries.com/v3.1/")
        .addConverterFactory(moshi)
        .build()

    @Singleton
    @Provides
    fun provideMoshi(): MoshiConverterFactory = MoshiConverterFactory.create()
}