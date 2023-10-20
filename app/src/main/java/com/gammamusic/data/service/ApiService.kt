package com.gammamusic.data.service

import com.gammamusic.data.model.SearchListResponse
import com.gammamusic.domain.model.Player.Track
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("track/{id}")
    suspend fun getTrack(@Path("id") id: Int): Response<Track>
    @GET("search")
    suspend fun getSearch(@Query("q") name:String):Response<SearchListResponse>
}