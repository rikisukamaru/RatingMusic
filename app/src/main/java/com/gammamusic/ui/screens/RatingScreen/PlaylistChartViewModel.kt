package com.gammamusic.ui.screens.RatingScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Playlist
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlaylistChartViewModel : ViewModel() {
    val playlists = mutableStateOf(listOf<Playlist>())

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
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
}
