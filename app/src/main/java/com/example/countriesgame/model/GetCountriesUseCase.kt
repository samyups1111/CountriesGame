package com.example.countriesgame.model

import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val localDataSource: LocalDataSource,
) {
    fun invoke(letter: Char): List<String> = localDataSource.countriesMap[letter] ?: emptyList()
}