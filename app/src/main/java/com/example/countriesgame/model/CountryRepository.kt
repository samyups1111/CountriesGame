package com.example.countriesgame.model

interface CountryRepository {

    suspend fun getCountryDescription(query: String): String
}