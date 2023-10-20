package com.gammamusic.data.model

import com.gammamusic.domain.model.Search.Search


data class SearchListResponse(
    val data: List<Search>
)