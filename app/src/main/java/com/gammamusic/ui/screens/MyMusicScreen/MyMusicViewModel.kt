package com.example.freelis.ui.screens.MyMusicScreen

import Search
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gammamusic.data.service.TrackApi
import com.gammamusic.domain.model.Track


import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

import retrofit2.Retrofit


class MyMusicViewModel : ViewModel() {


    private val _searchResult = MutableLiveData<List<Search>>()
    val searchResult: LiveData<List<Search>> = _searchResult

    private val _trackResult = MutableLiveData<List<Track>>()
    val trackResult: LiveData<List<Track>> = _trackResult
    fun getTrackResult() = viewModelScope.launch {

    }
}