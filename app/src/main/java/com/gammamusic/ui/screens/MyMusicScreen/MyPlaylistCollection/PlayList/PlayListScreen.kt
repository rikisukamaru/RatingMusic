@file:OptIn(ExperimentalMaterialApi::class)

package com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.PlayList

import android.util.Log

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check

import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import coil.compose.rememberAsyncImagePainter

import com.gammamusic.ui.screens.MusicPlayer.MusicPlayerScreen

import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PlayListScreen(viewModel: PlayListViewModel, selectedPlaylistId: String) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var isPlayerVisible by remember { mutableStateOf(false) }
    val sea by viewModel.trakes.observeAsState(initial = emptyList())

    var trackId by remember{
        mutableStateOf(0L)
    }
    LaunchedEffect(key1 = selectedPlaylistId) {
        viewModel.loadPlaylist(selectedPlaylistId)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Отображение данных из Firebase
        LazyColumn {
          itemsIndexed(sea){index, track ->
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
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 150.dp)
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ){
        IconButton(
            onClick = { scope.launch { sheetState.show() } },
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp)
                .border(2.dp, Color.Black, RectangleShape),

            ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Добавить"
            )
        }
        androidx.compose.material3.Button(
            onClick = {
                // Действие при нажатии на кнопк

                    viewModel.publishSelectedPlaylist(selectedPlaylistId)
                    // Добавьте сообщение об успешной публикации или обработку ошибок

            },
            modifier = Modifier
                .size(56.dp)
                .background(Color.Blue)
                .padding(8.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
        ) {
            androidx.compose.material.Text(text = "Опубликовать")
        }
    }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp) // Задайте желаемую высоту листа
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ){
               PlayListCreate(viewModel,selectedPlaylistId)
            }
        },
        sheetShape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
    ) {
    }
    if (isPlayerVisible) {
        MusicPlayerScreen(id =trackId)
    }





}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListCreate(viewModel: PlayListViewModel, selectedPlaylistId: String){
    var trackId by remember {
        mutableStateOf(0L)
    }
    val searches by viewModel.searches.observeAsState(emptyList())

    LazyColumn(modifier = Modifier.wrapContentSize(Alignment.TopCenter)) {
        itemsIndexed(searches) { index, search ->
            Card(
                onClick = {
                    trackId = search.id
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
                    .height(70.dp)
                    .background(Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth(0.2f)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = search.album.cover_medium
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
                        .wrapContentWidth(Alignment.Start)) {
                        Column() {
                            Text(text = search.title)
                            Text(text = search.artist.name)
                        }
                    }
                    IconButton(onClick = {

                        Log.i("aaaa",selectedPlaylistId)
                        if (selectedPlaylistId != null) {
                            viewModel.addSongToPlaylist(search.id,search.title,search.preview,search.artist.name,search.artist.id,search.album.cover_medium, selectedPlaylistId)

                        } else {
                            Log.i("PlayListNot","ты как сюда попал тебя сюда не звали))")
                        }}) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Delete")

                    }
                }
            }
        }
    }
}
