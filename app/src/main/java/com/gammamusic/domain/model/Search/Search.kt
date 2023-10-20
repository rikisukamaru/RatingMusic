package com.gammamusic.domain.model.Search

import com.gammamusic.data.model.SearchListResponse

data class Search (


    val id: Long,
    val readable: Boolean,
    val title: String,
   val title_short: String,
    val title_version: String,
    val link:String,
    val duration: Int,
    val rank: Int,
    val explicit_lyrics: Boolean,
    val explicit_content_lyrics: Int,
    val explicit_content_cover: Int,
    val preview:String


)