package com.example.freelis.ui.screens.MyMusicScreen.MyPlaylistCollection

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.PlayList.PlayListScreen
import com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.PlayList.PlayListViewModel
import com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.MyPlaylistCollectionViewModel
import kotlinx.coroutines.launch




@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable

fun MyPlaylistCollection(navController: NavController) {
    val viewModel: MyPlaylistCollectionViewModel = viewModel()
    val vviewModel: PlayListViewModel =viewModel()
    val playlists by viewModel.playlists.observeAsState(listOf())
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp) // Установите желаемую высоту
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                PlaylistForm(viewModel::createPlaylist)
                var playlistName by remember { mutableStateOf("") }

                Column {
                    TextField(
                        value = playlistName,
                        onValueChange = { playlistName = it },
                        label = { Text("Название плейлиста") }
                    )
                    Button(onClick = { viewModel.createPlaylist(playlistName) }) {
                        Text("Создать плейлист")
                    }
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Мои плейлисты") },
                    actions = {
                        IconButton(onClick = { coroutineScope.launch { sheetState.show() } }) {
                            Icon(Icons.Default.Add, contentDescription = "Добавить плейлист")
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Search, contentDescription = "Поиск")
                        }
                    }
                )
            },
            content = {
                LazyColumn {

                    itemsIndexed(playlists) { index, playlist ->
                        Card(onClick = {
                           navController.navigate("PlayList/${playlist.id}")
                        },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp)
                                .height(70.dp)
                                .background(Color.White)
                        ){
                            Text(playlist.name!!)
                        }

                    }
                    viewModel.loadPlaylists()
                }
            }

        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistForm(onCreatePlaylist: (String) -> Unit) {
    var playlistName by remember { mutableStateOf("") }

    Column {
        TextField(
            value = playlistName,
            onValueChange = { playlistName = it },
            label = { Text("Название плейлиста") }
        )
        Button(onClick = { onCreatePlaylist(playlistName) }) {
            Text("Создать плейлист")
        }
    }
}
