package com.example.countriesgame.model.repository

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.CountryRemote
import com.example.countriesgame.model.toCountry
import com.example.countriesgame.networking.RestCountriesService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val restCountriesService: RestCountriesService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CountryRepository {
    override suspend fun getCountriesFromServer(): List<Country> = withContext(ioDispatcher) {
        val response = restCountriesService.getAllCountries()
        if (response.isSuccessful) {
            processResponseBody(countriesRemote = response.body())
        } else {
            emptyList()
        }
    }

    private fun processResponseBody(countriesRemote: List<CountryRemote>?): List<Country> {
        return if (countriesRemote.isNullOrEmpty()) {
            emptyList()
        } else {
            val countries = mutableListOf<Country>()
            countriesRemote.forEach { countries.add(it.toCountry()) }
            countries.toList()
        }
    }
}