package com.gammamusic.ui.screens.RatingScreen.PublishedPlayList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Rating.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserChatViewModel: ViewModel() {
    val users = MutableLiveData<List<User>>()
    private val _users = MutableLiveData<List<User>>()



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
                                val ratingAuthor = user.ratingAuthor
                                val userObj = User(name = userName, ratingAuthor = ratingAuthor)
                                usersList.add(userObj)
                                println("User: $userName - Rating Author: $ratingAuthor")
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Обработка ошибки при чтении данных
                            }
                        })
                    }
                }
                _users.value = usersList
                users.value = usersList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки при чтении данных
            }
        })
    }


}
