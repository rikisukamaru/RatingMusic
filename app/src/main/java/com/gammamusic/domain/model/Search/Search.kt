package com.gammamusic.domain.model.Search

import com.gammamusic.domain.model.Player.Album
import com.gammamusic.domain.model.Player.Artist
import com.gammamusic.domain.model.Player.Track


data class Search (

    val id: Long= 0L,

    val title: String="",

    val preview:String="",

    val artist: Artist = Artist(),
    val track: Track = Track(),
    val album: Album = Album()

)