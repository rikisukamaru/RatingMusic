package com.gammamusic.ui.screens.RatingScreen.PublishedPlayList

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.gammamusic.domain.model.Player.Track
import com.gammamusic.domain.model.Playlist
import com.gammamusic.ui.screens.MusicPlayer.MusicPlayerScreen
@Composable
fun pbPlayList(navController: NavController, viewModel: pbPlayListViewModel, playlistId: String) {
    val playlist by viewModel.playlist.observeAsState()
    val tracks by viewModel.trakes.observeAsState(initial = emptyList())

    LaunchedEffect(key1 = playlistId) {
        viewModel.loadPlayList(playlistId)
    }

    // Отображение состояния загрузки
    if (playlist == null) {
        Text("Загрузка плейлиста...")
    } else if (tracks.isEmpty()) {
        Text("Треклист пуст")
    } else {
        // Отображение плейлиста и треклиста
        Column {
            Text(text = playlist?.name ?: "Неизвестный плейлист")
            LazyColumn {
                items(tracks.size) { index ->
                    val track = tracks[index]
                    TrackItem(track = track)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackItem(track: Track) {
    var isPlayerVisible by remember { mutableStateOf(false) }
    var trackId by remember{
        mutableStateOf(0L)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
                Card(onClick = {isPlayerVisible = true
                    trackId=track.songId
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
                    .height(40.dp)
                    .background(Color.White)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.fillMaxWidth(0.2f)) {
                            Image(
                                painter = rememberAsyncImagePainter(model = track.cover
                                    ?: "https://shutniks.com/wp-content/uploads/2020/01/unnamed-2.jpg"),
                                contentDescription = "avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(65.dp)
                                    .padding(8.dp)
                                    .wrapContentSize()
                            )
                        }

                        Box(modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .wrapContentWidth(Alignment.Start)){
                            Column() {
                                androidx.compose.material3.Text(text = track.title)
                                androidx.compose.material3.Text(text = track.nameArtist)
                            }
                        }
                    }

                }
            }
    if (isPlayerVisible) {
        MusicPlayerScreen(id =trackId)
    }

    }

