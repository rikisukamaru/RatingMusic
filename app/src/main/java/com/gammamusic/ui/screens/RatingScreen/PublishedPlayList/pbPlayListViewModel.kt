package com.gammamusic.ui.screens.RatingScreen.PublishedPlayList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gammamusic.domain.model.Player.Track
import com.gammamusic.domain.model.Playlist
import com.gammamusic.domain.model.Search.Search
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class pbPlayListViewModel: ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>()
    val trakes: LiveData<List<Track>> get() = _tracks

    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: MutableLiveData<Playlist?> = _playlist

    fun loadPlayList(playlistId: String) {
        // Получить ссылку на плейлисты в Firebase Realtime Database
        val playlistsRef = FirebaseDatabase.getInstance().getReference("charts/published")

        // Создать запрос для поиска плейлиста по id
        val query = playlistsRef.orderByChild("id").equalTo(playlistId)

        // Добавить слушателя изменений
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e(dataSnapshot.toString(), "onDataChange")
                var playlistData: Playlist? = null
                for (snapshot in dataSnapshot.children) {
                    val playlist = snapshot.getValue(Playlist::class.java)
                    if (playlist?.id == playlistId) {
                        playlistData = playlist
                        break
                    }
                }
                if (playlistData != null) {
                    _playlist.value = playlistData
                    _tracks.value = playlistData.tracklist.values.toList()
                } else {
                    Log.e("PbPlayListViewModel", "Плейлист с id $playlistId не найден")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработать ошибку
                Log.w("PbPlayListViewModel", "Ошибка загрузки плейлиста: ${databaseError.message}")
            }
        })
    }


}