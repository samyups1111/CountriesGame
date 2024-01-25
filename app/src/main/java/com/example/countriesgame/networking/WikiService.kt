package com.example.countriesgame.networking

import com.example.countriesgame.model.WikiResponseObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * **See:** [wiki query doc](https://www.mediawiki.org/w/api.php?action=help&modules=query)
 */
interface WikiService {

    @GET("w/api.php?")
    suspend fun getCountryData(
        @Query("page") query: String,
        @Query("action") action: String = "parse",
        @Query("format") format: String = "json",
        @Query("prop") prop: String = "text",
    ): Response<WikiResponseObject>
}