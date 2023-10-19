package com.gammamusic.data.service

import com.gammamusic.domain.model.Track
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {

    @GET("track/{id}")
    suspend fun getTrack(@Path("id") id: Int): Response<Track>
}