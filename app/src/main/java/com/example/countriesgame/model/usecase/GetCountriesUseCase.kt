package com.example.countriesgame.model.usecase

import com.example.countriesgame.model.Country
import com.example.countriesgame.model.repository.CountryRepository
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repository: CountryRepository,
) {
    suspend fun invoke(): List<Country> {

        return repository.getCountriesFromServer()
    }
}