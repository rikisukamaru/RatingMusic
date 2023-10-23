package com.gammamusic.domain.model.Player

import com.gammamusic.domain.model.Player.Album
import com.gammamusic.domain.model.Player.Artist

data class Track (
    val id: Long,

    val title : String,

    val link:String,


    val preview: String,


    val album: Album,
    val artist: Artist
)