package com.gammamusic.domain.model

data class Playlist(
    val id: String = "",
    val name: String? = "",
    val songs: List<String> = listOf(),
    val photoUrl: String? = "",
    val rating: Int = 0,
    var published: Boolean = false
)