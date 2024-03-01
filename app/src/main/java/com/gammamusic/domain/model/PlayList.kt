package com.gammamusic.domain.model

data class Playlist(
    var id: String = "",
    var name: String? = "",
    var songs: List<String> = listOf(),
    var photoUrl: String? = "",
    var rating: Int = 0,
    var published: Boolean = false
)
