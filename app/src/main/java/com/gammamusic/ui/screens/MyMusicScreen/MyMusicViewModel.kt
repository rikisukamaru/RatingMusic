package com.example.freelis.ui.screens.MyMusicScreen

import android.util.Log
import androidx.lifecycle.LiveData
import com.gammamusic.domain.model.Search.Search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gammamusic.data.service.ApiService
import com.gammamusic.domain.model.Player.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter


import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyMusicViewModel : ViewModel() {
    private val apiService: ApiService
    val searchQuery = MutableStateFlow("")
    init {
        val apikey = com.gammamusic.BuildConfig.KEY
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-RapidAPI-Key", apikey)
                    .addHeader("X-RapidAPI-Host", "deezerdevs-deezer.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        apiService = retrofit.create(ApiService::class.java)
        viewModelScope.launch {
            searchQuery
                .debounce(300) // Задержка в 300 миллисекунд
                .filter { it.isNotBlank() } // Игнорировать пустые запросы
                .distinctUntilChanged() // Игнорировать повторяющиеся запросы
                .collect { query ->
                    search(query)
                }
        }
    }


    private val _searchLiveData = MutableLiveData<List<Search>>()
    val searchLiveData: LiveData<List<Search>> = _searchLiveData

    fun search(nameArtist:String) = viewModelScope.launch {
        try {
            if (nameArtist.isBlank()) {
                _searchLiveData.value = emptyList() // Очистить список результатов
                return@launch
            }
        }catch(e: Exception) {
            Log.e("xcv",e.message.toString())
        }
        try {
            val response = apiService.getSearch(nameArtist)
            Log.e("asdf",response.toString())
            if (response.isSuccessful) {
                val search = response.body()
                _searchLiveData.value = search?.data
            } else {
                // Обработка ошибки
            }
        } catch (e: Exception) {
            Log.e("xcv",e.message.toString())
        }
    }
    fun clearSearchResults() {
        _searchLiveData.value = emptyList()
    }
}