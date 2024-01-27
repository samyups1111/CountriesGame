package com.example.countriesgame.networking

import com.example.countriesgame.model.CountryRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * **See:** [wiki query doc](https://www.mediawiki.org/w/api.php?action=help&modules=query)
 */
interface WikiService {

    @GET("independent?")
    suspend fun getAllCountries(
        @Query("fields") fields: String = "capital,region,flag,maps,name,population,unMember,cca3,coatOfArms,languages,borders,currencies"
    ): Response<List<CountryRemote>>
}