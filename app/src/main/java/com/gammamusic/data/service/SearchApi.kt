package com.example.freelis.data.service


import com.gammamusic.data.model.SearchListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("api/v1/json/2/search.php")
    suspend fun getSearchById(@Query("s") s: String = "coldplay"): Response<SearchListResponse>

}