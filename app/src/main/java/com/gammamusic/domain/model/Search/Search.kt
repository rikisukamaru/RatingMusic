package com.gammamusic.domain.model.Search


data class Search (

    val id: Long= 0L,

    val title: String="",

    val preview:String=""


){
    constructor() : this(0L, "")
}