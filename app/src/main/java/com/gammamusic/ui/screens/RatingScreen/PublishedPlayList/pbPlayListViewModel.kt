package com.gammamusic.ui.screens.RatingScreen.PublishedPlayList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Player.Track
import com.gammamusic.domain.model.Playlist
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

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

    fun updatePlaylistRating(playlistId: String, ratingChange: Int) {
        val database = FirebaseDatabase.getInstance()
        val playlistsRef = database.getReference("charts/published")

        // Создать запрос для поиска плейлиста по id
        val query = playlistsRef.orderByChild("id").equalTo(playlistId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Получаем ссылку на первый найденный плейлист
                    val playlistRef = dataSnapshot.children.first().ref

                    playlistRef.runTransaction(object : Transaction.Handler {
                        override fun doTransaction(mutableData: MutableData): Transaction.Result {
                            val playlist = mutableData.getValue(Playlist::class.java)
                            if (playlist == null) {
                                return Transaction.success(mutableData)
                            }

                            // Обновляем рейтинг
                            val newRating = playlist.rating + ratingChange
                            Log.e("newRating", newRating.toString())
                            playlist.rating = newRating

                            // Обновляем данные в Firebase
                            mutableData.value = playlist
                            return Transaction.success(mutableData)
                        }

                        override fun onComplete(databaseError: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                            // Обработка ошибок или действий после обновления
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок
            }
        })
    }
    fun loadSwipeCount(playlistId: String, userId: String) {
        val database = FirebaseDatabase.getInstance()
        val playlistsRef = database.getReference("charts/published/$playlistId/swipeCounts/$userId")

        playlistsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val swipeCount = snapshot.getValue(Int::class.java) ?: 0
                    // Здесь обработать полученное количество свайпов
                    // Например, сохранить его в LiveData или использовать напрямую
                    Log.d("SwipeCount", "Current swipe count for user $userId: $swipeCount")
                } else {
                    // Если для пользователя еще нет записи, считаем, что свайпов не было
                    Log.d("SwipeCount", "No swipe count found for user $userId")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок
            }
        })
    }

    fun increaseSwipeCount(playlistId: String, userId: String) {
        val database = FirebaseDatabase.getInstance()
        val playlistsRef = database.getReference("charts/published/$playlistId/swipeCounts/$userId")

        playlistsRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val playlist = mutableData.getValue(Playlist::class.java)
                if (playlist == null) {
                    return Transaction.success(mutableData)
                }

                // Увеличиваем счетчик свайпов для пользователя
                val swipeCounts = (playlist.swipeCounts ?: mutableMapOf()).toMutableMap()
                val currentCount = swipeCounts[userId] ?: 0
                swipeCounts[userId] = currentCount + 1
                playlist.swipeCounts = swipeCounts

                mutableData.value = playlist
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                if (committed) {
                    Log.d("SwipeCount", "Swipe count increased for user $userId")
                } else {
                    Log.w("SwipeCount", "Failed to increase swipe count for user $userId")
                }
            }
        })
    }

}