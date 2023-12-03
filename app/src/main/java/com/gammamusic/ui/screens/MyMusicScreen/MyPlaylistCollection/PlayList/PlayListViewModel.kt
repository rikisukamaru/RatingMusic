package com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.PlayList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Player.Track
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

    init {

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

    fun addSongToPlaylist(songId: Long,title:String,preview:String,nameArtist:String,idArtist:Long,cover:String, playlistId: String) {
        val database = Firebase.database
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userPlaylistRef = database.getReference("users/$userId/playlists/$playlistId/tracklist")

        // Проверяем, есть ли уже такая песня в tracklist
        userPlaylistRef.child("songs").addListenerForSingleValueEvent(object : ValueEventListener {
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
                    val newSongRef = userPlaylistRef.child("songs").push()
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
            val playlistRef = database.getReference("users/$userId/playlists/$playlistId/tracklist/songs")

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