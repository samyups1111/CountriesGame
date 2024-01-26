package com.example.countriesgame.model

import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repository: CountryRepository,
) {
    suspend fun invoke(): List<Country> {

        return repository.getCountriesFromServer()
    }
}