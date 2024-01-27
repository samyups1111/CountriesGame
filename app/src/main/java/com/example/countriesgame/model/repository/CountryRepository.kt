package com.example.countriesgame.model.repository

import com.example.countriesgame.model.Country

interface CountryRepository {
    suspend fun getCountriesFromServer(): List<Country>
}