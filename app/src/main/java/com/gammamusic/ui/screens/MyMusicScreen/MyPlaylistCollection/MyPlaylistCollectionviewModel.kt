package com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection

import android.net.Uri

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Playlist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class MyPlaylistCollectionViewModel: ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val chartsRef = database.getReference("charts")



    fun createPlaylist(playlistName: String, imageUri: Uri?,genre:String) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val storage = FirebaseStorage.getInstance().reference
        if (userId != null) {
            val playlistId = UUID.randomUUID().toString()
            val ref = database.getReference("users/$userId/playlists/$playlistId")

            // Проверяем, есть ли изображение
            if (imageUri != null) {
                // Получаем ссылку на Firebase Storage
                val storageRef = storage
                val imagesRef = storageRef.child("images/$playlistId.jpg")

                // Загружаем изображение в Storage
                imagesRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        // Получаем URL-адрес загруженного изображения
                        imagesRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()

                            // Создаем объект Playlist с URL-адресом изображения
                            val playlist = Playlist(playlistId, playlistName, imageUrl, genre)

                            // Сохраняем Playlist в базу данных
                            ref.setValue(playlist)
                                .addOnSuccessListener {
                                    // Успешно сохранено
                                }
                                .addOnFailureListener { error ->
                                    // Ошибка сохранения
                                }
                        }
                    }
                    .addOnFailureListener { error ->
                        // Ошибка загрузки изображения в Storage
                    }
            } else {
                // Если изображение не было выбрано, сохраняем плейлист без изображения
                val playlist = Playlist(playlistId, playlistName, "",genre)
                ref.setValue(playlist)
                    .addOnSuccessListener {
                        // Успешно сохранено
                    }
                    .addOnFailureListener { error ->
                        // Ошибка сохранения
                    }
            }
        }
    }



    fun loadPlaylists() {
        chartsRef.setValue(null)
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