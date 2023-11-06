package com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Playlist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.UUID

class MyPlaylistCollectionViewModel: ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun createPlaylist(playlistName: String) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val playlistId = UUID.randomUUID().toString()
            val ref = database.getReference("users/$userId/playlists/$playlistId")

            val playlist = Playlist(playlistId, playlistName)
            ref.setValue(playlist)
                .addOnSuccessListener {
                    // Playlist created successfully
                }
                .addOnFailureListener { error ->
                    // Error creating playlist
                }
        }
    }


    fun loadPlaylists() {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val ref = database.getReference("users/$userId/playlists")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val playlists = mutableListOf<Playlist>()
                    for (postSnapshot in dataSnapshot.children) {
                        val playlist = postSnapshot.getValue<Playlist>()
                        if (playlist != null) {
                            playlists.add(playlist)
                        }
                    }
                    _playlists.value = playlists
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error here
                }
            })
        }
    }
}