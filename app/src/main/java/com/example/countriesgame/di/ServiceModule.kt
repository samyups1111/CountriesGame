package com.example.countriesgame.di

import com.example.countriesgame.networking.WikiService
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
    fun provideNasaService(
        retrofit: Retrofit,
    ): WikiService = retrofit.create(WikiService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(
        moshi: MoshiConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://en.wikipedia.org/")
        .addConverterFactory(moshi)
        .build()

    @Singleton
    @Provides
    fun provideMoshi(): MoshiConverterFactory = MoshiConverterFactory.create()
}