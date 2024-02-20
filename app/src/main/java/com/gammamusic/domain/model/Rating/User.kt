package com.gammamusic.domain.model.Rating

data class User(
    val id: String,
    val name: String,
    val email: String,
    val playlists: List<String>,
    val rating: Int
)
