package com.gammamusic.ui.screens.MyMusicScreen.MyMusicCollection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.gammamusic.domain.model.Player.Track

import com.gammamusic.ui.screens.MusicPlayer.MusicPlayerScreen
import com.gammamusic.ui.screens.MusicPlayer.TrackViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMusicCollection(viewModel: MyMusicCollectionViewModel) {
    val searches by viewModel.searches.observeAsState(emptyList())
    var isPlayerVisible by remember { mutableStateOf(false) }
    isPlayerVisible=true
    var trackId by remember{
        mutableStateOf(0L)
    }
    // Ваш код для отображения списка песен
    LazyColumn (modifier = Modifier.padding(bottom = 150.dp)){
        itemsIndexed(searches) { index, search ->
            Card(onClick = {
            trackId = search.id
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
                    .height(70.dp)
                    .background(Color.White)
                ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
                    Box(modifier =  Modifier.fillMaxWidth(0.2f)){
                        Image(
                            painter = rememberAsyncImagePainter(model =search.album.cover_medium?:"https://shutniks.com/wp-content/uploads/2020/01/unnamed-2.jpg"),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Crop,            // crop the image if it's not a square
                            modifier = Modifier

                                .size(65.dp)
                                .padding(8.dp)
                                .wrapContentSize()   // add a border (optional)
                        )
                    }
                    Box(modifier =  Modifier.fillMaxWidth(0.4f).wrapContentWidth(Alignment.Start)){
                        Column() {
                            Text(text = search.title)
                            Text(text = search.artist.name)
                        }
                    }

                    IconButton(onClick = {
                        viewModel.deleteSongFromCollection(search.id)}) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")

                    }
                }

            }


        }
    }
    if (isPlayerVisible) {
        MusicPlayerScreen(id = trackId)
    }
}

