package com.gammamusic.ui.screens.RatingScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Playlist
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

class PlaylistChartViewModel : ViewModel() {
    val playlists = mutableStateOf(listOf<Playlist>())

    init {
        loadPlaylists()
    }

     fun loadPlaylists() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("charts/published")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val playlistList = mutableListOf<Playlist>()
                for (snapshot in dataSnapshot.children) {
                    val playlist = snapshot.getValue(Playlist::class.java)
                    playlist?.let { playlistList.add(it) }
                }
                playlists.value = playlistList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки
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

}
