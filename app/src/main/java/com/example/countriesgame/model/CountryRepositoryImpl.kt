package com.example.countriesgame.model

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
            val countries = response.body()

            if (countries.isNullOrEmpty()) {
                emptyList()
            } else {
                countries
            }
        } else {
            emptyList()
        }
    }
}