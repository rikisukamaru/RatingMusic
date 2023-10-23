package com.gammamusic.ui.screens.MusicPlayer

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gammamusic.data.service.ApiService
import com.gammamusic.domain.model.Player.Track
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TrackViewModel : ViewModel() {
    private val apiService: ApiService
    private var mediaPlayer: MediaPlayer? = null

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
    }

    private val _trackLiveData = MutableLiveData<Track?>()
    val trackLiveData: LiveData<Track?> = _trackLiveData



    fun play() {
        trackLiveData.value?.let { track ->
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(track.preview)
                prepareAsync() // might take long! (for buffering, etc)
                setOnPreparedListener {
                    start()
                }
            }
        }
    }
    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }


    fun getTrack(id: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.getTrack(id)
                Log.e("asdf",response.toString())
                if (response.isSuccessful) {
                    val track = response.body()
                    _trackLiveData.value = track
                } else {
                    // Обработка ошибки
                }
            } catch (e: Exception) {
               Log.e("aaaa",e.message.toString())
            }
        }
    }
}