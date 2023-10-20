package com.gammamusic.domain.model.Player

import com.gammamusic.domain.model.Player.Album
import com.gammamusic.domain.model.Player.Artist

data class Track (
    val id: Int,
    val readable: Boolean,
    val title : String,
    val title_short:String,
    val title_version:String,
    val isrc:String,
    val link:String,
    val share:String,
    val duration:Int,
    val track_position:Int,
    val disk_number:Int,
    val rank:Int,
    val release_date: String,
    val explicit_lyrics:Boolean,
    val explicit_content_lyrics:Int,
    val explicit_content_cover:Int,
    val preview: String,
    val bpm:Int,
    val gain:Int,

    val album: Album,
    val artist: Artist
)