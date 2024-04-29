package com.gammamusic.domain.model

import com.gammamusic.domain.model.Player.Track

data class Playlist(
    var id: String = "",
    var name: String? = "",
    var photoUrl: String? = "",
    val genre:String? = "",
    var tracklist: Map<String,Track> = mapOf(),
    var rating: Int = 0,
    var userId: String? = null,
    var published: Boolean = true,
    var swipeCounts: Map<String, Int>? = null,

)
