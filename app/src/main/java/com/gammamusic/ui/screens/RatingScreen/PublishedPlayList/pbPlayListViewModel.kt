package com.gammamusic.ui.screens.RatingScreen.PublishedPlayList

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Player.Track
import com.gammamusic.domain.model.Playlist
import com.google.firebase.auth.FirebaseAuth
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

    fun updateTrackRating(context: Context, playlistId: String, trackId: String, ratingChange: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance()
        val userTrackSwipesRef = database.getReference("userSwipes/$userId/trackSwipes/$playlistId/$trackId")
        val userTrackSwipeCountsRef = database.getReference("userSwipes/$userId/trackSwipes/$playlistId")

        userTrackSwipeCountsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val uniqueTrackSwipeCount = dataSnapshot.childrenCount

                if (uniqueTrackSwipeCount < 2 || dataSnapshot.hasChild(trackId)) {
                    userTrackSwipesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val trackSwipeCount = dataSnapshot.getValue(Int::class.java) ?: 0

                            if (trackSwipeCount < 1) {
                                // Пользователь еще не свайпал этот трек
                                userTrackSwipesRef.setValue(trackSwipeCount + 1)

                                // Обновляем рейтинг трека аналогично обновлению рейтинга плейлиста
                                val playlistsRef = database.getReference("charts/published")
                                val query = playlistsRef.orderByChild("id").equalTo(playlistId)

                                query.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val playlistRef = dataSnapshot.children.first().ref
                                            val trackRef = playlistRef.child("tracklist").child(trackId)

                                            trackRef.runTransaction(object : Transaction.Handler {
                                                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                                    val track = mutableData.getValue(Playlist::class.java)
                                                    if (track == null) {
                                                        return Transaction.success(mutableData)
                                                    }

                                                    // Обновляем рейтинг
                                                    val newRating = track.rating + ratingChange
                                                    track.rating = newRating

                                                    mutableData.value = track
                                                    return Transaction.success(mutableData)
                                                }

                                                override fun onComplete(databaseError: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                                                    // Отображение Toast сообщения после обновления рейтинга
                                                    if (committed) {
                                                        Toast.makeText(context, "Рейтинг трека обновлен", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        Toast.makeText(context, "Ошибка обновления рейтинга", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            })
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Обработка ошибок
                                        Toast.makeText(context, "Ошибка загрузки данных плейлиста", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            } else {
                                // Пользователь уже свайпал этот трек, выводим сообщение
                                Toast.makeText(context, "Вы уже свайпали этот трек", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Обработка ошибок
                            Toast.makeText(context, "Ошибка загрузки данных трека", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    // Пользователь уже свайпал два уникальных трека в этом плейлисте, выводим сообщение
                    Toast.makeText(context, "Вы уже свайпали два уникальных трека в этом плейлисте", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок
                Toast.makeText(context, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
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