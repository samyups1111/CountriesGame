package com.example.countriesgame.model

interface CountryRepository {
    suspend fun getCountriesFromServer(): List<Country>
}