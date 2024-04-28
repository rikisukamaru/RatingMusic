package com.gammamusic.ui.screens.MyNewMusicScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Playlist
import com.gammamusic.domain.model.Rating.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyProfileScreenViewModel: ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists
    init {
        loadUserData()
       loadUserPlaylists()
    }

    private fun loadUserData() {
        // Здесь должен быть ваш код для получения данных пользователя
        // Например, из Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val photoUrl = currentUser.photoUrl?.toString() ?: ""
            // Замена s96-c на s400-c для получения изображения с более высоким разрешением
            val highResPhotoUrl = photoUrl.replace("s96-c", "s600-c")
            _user.value = User(
                name = currentUser.displayName ?: "Unknown",
                email = currentUser.email ?: "Unknown",
                id = currentUser.uid,
                playlists = listOf(),
                rating = 0,
                photoUrl = highResPhotoUrl
            )
        }
    }
    private fun loadUserPlaylists() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance()
        val playlistsRef = database.getReference("charts/published")

        playlistsRef.orderByChild("userId").equalTo(userId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val playlists = snapshot.children.mapNotNull { it.getValue(Playlist::class.java) }
                _playlists.value = playlists
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки
            }
        })
    }
}