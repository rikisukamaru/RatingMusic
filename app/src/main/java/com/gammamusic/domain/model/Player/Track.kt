package com.gammamusic.domain.model.Player

import com.gammamusic.domain.model.Player.Album
import com.gammamusic.domain.model.Player.Artist

data class Track (
    val id: Long = 0L,
    val idArtist: Long = 0L,
    val nameArtist:String="",
    val title : String="",
    val cover:String="",
    val preview: String="",
    val songId:Long=0L,
    val link:String="",
    val album: Album=Album(),
    val artist: Artist= Artist()
)