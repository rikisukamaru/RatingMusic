package com.gammamusic.domain.model

import com.gammamusic.domain.model.Player.Track

data class Playlist(
    var id: String = "",
    var name: String? = "",
    var tracklist: Map<String,Track> = mapOf(),
    var photoUrl: String? = "",
    var rating: Int = 0,
    var published: Boolean = false
)