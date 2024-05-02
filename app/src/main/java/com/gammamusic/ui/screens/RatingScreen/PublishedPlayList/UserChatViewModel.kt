package com.gammamusic.ui.screens.RatingScreen.PublishedPlayList


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Rating.User

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserChatViewModel: ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>  = _users



    fun loadTopUsers() {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        val usersList = mutableListOf<User>()
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    val userId = userSnapshot.key // Получаем UID пользователя

                    if (user != null && user.ratingAuthor != null && user.ratingAuthor != 0) {
                        // Получаем имя пользователя по UID из базы данных
                        val authorNameRef = database.getReference("users/$userId/authorname")
                        authorNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userName = snapshot.getValue(String::class.java) ?: "Unknown"
                                val photoUserRef = database.getReference("users/$userId/photouser")
                                photoUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(photoSnapshot: DataSnapshot) {
                                        val photouser = photoSnapshot.getValue(String::class.java) ?: ""
                                        val highResPhotoUrl = photouser.replace("s96-c", "s600-c")
                                        val ratingAuthor = user.ratingAuthor
                                        val userObj = User(name = userName, ratingAuthor = ratingAuthor, photoUrl = highResPhotoUrl)
                                        usersList.add(userObj)
                                        _users.value = usersList
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Обработка ошибки при чтении данных
                                    }
                                })
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Обработка ошибки при чтении данных
                            }
                        })
                    }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки при чтении данных
            }
        })
    }


}
