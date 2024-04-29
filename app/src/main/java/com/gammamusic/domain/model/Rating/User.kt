package com.gammamusic.domain.model.Rating

data class User(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String,
    val playlists: List<String>,
    val ratingAuthor: Int,
    val publishedPlaylistCount: Int
)
