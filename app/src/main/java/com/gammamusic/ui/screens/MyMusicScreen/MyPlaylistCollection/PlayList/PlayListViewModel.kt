package com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.PlayList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class PlayListViewModel:ViewModel(){
    private val _searches = MutableLiveData<List<Search>>()
    val searches: LiveData<List<Search>> get() = _searches
    private val _tracks = MutableLiveData<List<Track>>()
    val trakes: LiveData<List<Track>> get() = _tracks
    private val _publishedPlaylistCount = MutableLiveData<Int>()


    private val _totalRating = MutableLiveData<Int>()

    init {
        _publishedPlaylistCount.value = 0
        _totalRating.value = 0
        // Получить данные из Firebase
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users").child(userId).child("searches")

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val searchList = mutableListOf<Search>()
                    for (searchSnapshot in dataSnapshot.children) {
                        val search = searchSnapshot.getValue(Search::class.java)
                        if (search != null) {
                            searchList.add(search)
                        }
                    }
                    _searches.value = searchList
                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибки
                }
            })
        }
    }


    fun publishSelectedPlaylist(playlistId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users/${user.uid}")

            userRef.child("playlistCount").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val count = dataSnapshot.getValue(Int::class.java) ?: 0

                    if (count >= 5) {
                        // Публикация шестого плейлиста, пересчет рейтинга автора
                        val userRatingRef = database.getReference("users/${user.uid}/ratingAuthor")
                        val publishedPlaylistsRef = database.getReference("charts/published")

                        publishedPlaylistsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                var totalRating = 0
                                var playlistCount = 0
                                for (playlistSnapshot in dataSnapshot.children) {
                                    val playlist = playlistSnapshot.getValue(Playlist::class.java)
                                    if (playlist != null && playlist.userId == user.uid) {
                                        totalRating += playlist.rating
                                        playlistCount++
                                    }
                                }
                                val authorRating = if (playlistCount > 0) totalRating / playlistCount else 0
                                userRatingRef.setValue(authorRating)
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Обработка ошибки при чтении данных
                            }
                        })
                    }

                    // Увеличить счетчик опубликованных плейлистов пользователя
                    userRef.child("playlistCount").setValue(count + 1)

                    // Публикация плейлиста
                    val playlistRef = database.getReference("users/${user.uid}/playlists/$playlistId")
                    playlistRef.get().addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val playlist = snapshot.getValue(Playlist::class.java)
                            if (playlist != null) {
                                playlist.userId = user.uid
                                val reference = database.getReference("charts/published").push()
                                reference.setValue(playlist)
                            } else {
                                // Обработка ошибки преобразования данных
                            }
                        } else {
                            // Обработка ошибки, если плейлист не найден
                        }
                    }.addOnFailureListener {
                        // Обработка ошибки при чтении данных из Firebase
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработка ошибки при чтении данных
                }
            })
        } else {
            // Обработка ошибки авторизации
        }
    }


    fun addSongToPlaylist(songId: Long,title:String,preview:String,nameArtist:String,idArtist:Long,cover:String, playlistId: String) {
        val database = Firebase.database
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userPlaylistRef = database.getReference("users/$userId/playlists/$playlistId/tracklist")

        // Проверяем, есть ли уже такая песня в tracklist
        userPlaylistRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isSongAlreadyAdded = false

                for (songSnapshot in dataSnapshot.children) {
                    val existingSongId = songSnapshot.child("songId").getValue(Long::class.java)
                    if (existingSongId == songId) {
                        // Песня уже добавлена
                        isSongAlreadyAdded = true
                        break
                    }
                }

                if (!isSongAlreadyAdded) {
                    // Если песни еще нет в tracklist, добавляем ее
                    val newSongRef = userPlaylistRef.push()
                    newSongRef.child("songId").setValue(songId)
                    newSongRef.child("title").setValue(title)
                    newSongRef.child("preview").setValue(preview)
                    newSongRef.child("nameArtist").setValue(nameArtist)
                    newSongRef.child("idArtist").setValue(idArtist)
                    newSongRef.child("cover").setValue(cover)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки при чтении данных из Firebase
            }
        })
    }
    fun loadPlaylist(playlistId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val database = Firebase.database
            val playlistRef = database.getReference("users/$userId/playlists/$playlistId/tracklist")

            playlistRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val trackList = mutableListOf<Track>()
                    for (trackSnapshot in dataSnapshot.children) {
                        val track = trackSnapshot.getValue(Track::class.java)
                        if (track != null) {
                            trackList.add(track)
                        }
                    }
                    _tracks.value = trackList
                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибки
                }
            })
        }
    }

}