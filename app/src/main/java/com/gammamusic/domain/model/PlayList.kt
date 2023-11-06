package com.gammamusic.domain.model

data class Playlist(
    var id: String = "",
    var name: String? = "",
    var songs: List<String> = listOf(),
    val photoUrl: String? = "",
)