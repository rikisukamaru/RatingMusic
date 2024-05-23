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

class MyProfileScreenViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists
    private val database = FirebaseDatabase.getInstance()
    init {
        loadUserDetails()
        loadUserPlaylists()
    }

     fun loadUserDetails() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser!= null) {
            val userId = currentUser.uid
            val photoUrl = currentUser.photoUrl?.toString()?: ""
            val highResPhotoUrl = photoUrl.replace("s96-c", "s600-c")

            // Загрузка рейтинга пользователя
            val userRef = database.getReference("users/$userId/ratingAuthor")
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val rating = snapshot.getValue(Int::class.java)?: 0
                    // Загрузка других деталей пользователя
                    val displayName = currentUser.displayName?: "Unknown"
                    val email = currentUser.email?: "Unknown"

                    _user.postValue(User(
                        name = displayName,
                        email = email,
                        id = userId,
                        ratingAuthor = rating,
                        photoUrl = highResPhotoUrl,
                        publishedPlaylistCount = 0
                    ))
                }

                override fun onCancelled(error: DatabaseError) {
                    // Обработка ошибок
                }
            })
        }
    }

    private fun loadUserPlaylists() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid?: return
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
