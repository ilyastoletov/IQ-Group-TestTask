package com.ilyastoletov.data.network.api

import com.ilyastoletov.data.network.dto.AreaResponse
import com.ilyastoletov.data.network.dto.DictionariesResponse
import com.ilyastoletov.data.network.dto.vacancy.PagedVacanciesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface VacancyApi {

    @GET("/dictionaries")
    fun getDictionaries(): Call<DictionariesResponse>

    @GET("/areas")
    fun getAreas(): Call<List<AreaResponse>>

    @GET
    fun getVacancies(@Url requestUrl: String): Call<PagedVacanciesResponse>

}
