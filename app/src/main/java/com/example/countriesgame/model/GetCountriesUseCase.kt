package com.example.countriesgame.model

import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val repository: CountryRepository,
) {
    fun invoke(letter: Char): List<Country> {

        val countriesString = localDataSource.countriesMap[letter] ?: emptyList()
        return countriesString.map { name ->
            name.toCountry()
        }
    }

    suspend fun getDescription(name: String): String = repository.getCountryDescription(name)
}