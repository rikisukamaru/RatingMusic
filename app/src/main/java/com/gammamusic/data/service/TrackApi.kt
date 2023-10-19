package com.gammamusic.data.service

import com.gammamusic.domain.model.Track
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface TrackApi {
        @GET("track/{id}")
        fun getTrack(
                @Header("X-RapidAPI-Key") apiKey: String,
                @Header("X-RapidAPI-Host") apiHost: String,
                @Path("id") id: Int
        ): Response<Track>
}