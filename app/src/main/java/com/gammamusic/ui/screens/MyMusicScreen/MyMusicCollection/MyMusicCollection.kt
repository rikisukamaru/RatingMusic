package com.gammamusic.ui.screens.MyMusicScreen.MyMusicCollection

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.gammamusic.domain.model.Search.Search
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun MyMusicCollection(viewModel: MyMusicCollectionViewModel) {
    val searches by viewModel.searches.observeAsState(emptyList())

    // Ваш код для отображения списка песен
    LazyColumn {
        itemsIndexed(searches) { index, search ->
            Text(text = search.title)
        }
    }
}
