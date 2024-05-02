package com.gammamusic.domain.model.Rating

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",

    val ratingAuthor: Int = 0,
    val publishedPlaylistCount: Int = 0
) {
    constructor() : this("", "", "", "",  0, 0)
}