package com.example.countriesgame.model.repository

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.toCountry
import com.example.countriesgame.networking.WikiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val wikiService: WikiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CountryRepository {
    override suspend fun getCountriesFromServer(): List<Country> = withContext(ioDispatcher) {
        val response = wikiService.getAllCountries()

        if (response.isSuccessful) {
            val countriesRemote = response.body()

            if (countriesRemote.isNullOrEmpty()) {
                emptyList()
            } else {
                val countries = mutableListOf<Country>()
                countriesRemote.forEach { countries.add(it.toCountry()) }
                countries.toList()
            }
        } else {
            emptyList()
        }
    }
}