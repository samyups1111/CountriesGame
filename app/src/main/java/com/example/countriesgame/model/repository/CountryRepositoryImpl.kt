package com.example.countriesgame.model.repository

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.CountryRemote
import com.example.countriesgame.networking.CountriesService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val countriesService: CountriesService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CountryRepository {

    override suspend fun getCountriesFromServer(): List<Country> = withContext(ioDispatcher) {
        val response = countriesService.getAllCountries()
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
            countriesRemote.forEach { countries.add(it.toDomain()) }
            countries.toList()
        }
    }

    private fun CountryRemote.toDomain(): Country {

        val languages = mutableListOf<String>()
        val currencies = mutableListOf<String>()
        this.languages.values.forEach { language ->
            languages.add(language)
        }
        this.currencies.values.forEach {  currency ->
            currencies.add(currency.symbol + " " + currency.name)
        }

        return Country(
            id = this.id,
            name = this.name,
            capital = this.capital,
            coatOfArms = this.coatOfArms,
            flag = this.flag,
            languages = languages,
            maps = this.maps,
            population = this.population,
            region = this.region,
            unMember = this.unMember,
            currencies = currencies,
            borders = this.borders,
        )
    }
}