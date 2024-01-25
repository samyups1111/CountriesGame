package com.example.countriesgame.model

import android.util.Log
import com.example.countriesgame.networking.WikiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val wikiService: WikiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CountryRepository {
    override suspend fun getCountryDescription(query: String): String = withContext(ioDispatcher) {
        val response = wikiService.getCountryData(query)

        if (response.isSuccessful) {
            val body = response.body()
            val text = body?.parse?.text?.description
            log(text ?: "nothing found")

            text ?: "No data found"
        } else {
            "Network error"
        }
    }
}

fun log(text: String) = Log.d("Sammy", text)