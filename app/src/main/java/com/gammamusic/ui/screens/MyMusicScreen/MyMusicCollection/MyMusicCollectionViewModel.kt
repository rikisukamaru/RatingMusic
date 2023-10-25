package com.gammamusic.ui.screens.MyMusicScreen.MyMusicCollection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gammamusic.domain.model.Search.Search
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyMusicCollectionViewModel:ViewModel() {
    private val _searches = MutableLiveData<List<Search>>()
    val searches: LiveData<List<Search>> get() = _searches

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
}