package com.example.freelis.ui.screens.MyMusicScreen.MyPlaylistCollection

import android.annotation.SuppressLint

import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
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


import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

import com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.MyPlaylistCollectionViewModel
import kotlinx.coroutines.launch




@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable

fun MyPlaylistCollection(navController: NavController) {
    val viewModel: MyPlaylistCollectionViewModel = viewModel()
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
                            Image(painter = rememberAsyncImagePainter(model = playlist.photoUrl), contentDescription =" ")
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
fun PlaylistForm(onCreatePlaylist: (String, Uri?, String) -> Unit) {
    var playlistName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedGenre by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }

    val genres = listOf("rap", "hip-hop", "alternative", "pop", "r&b", "rock", "classic", "indi", "techno")

    // Запрос на выбор изображения
    val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Column {
        TextField(
            value = playlistName,
            onValueChange = { playlistName = it },
            label = { Text("Название плейлиста") }
        )

        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text("Выбрать изображение")
        }

        imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = "Выбранное изображение",
                modifier = Modifier.size(100.dp)
            )
        }

        // Dropdown menu for selecting genre
        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false }
        ) {
            genres.forEach { genre ->
                DropdownMenuItem(onClick = {
                    selectedGenre = genre
                    showDropdown = false
                }) {
                    Text(text = genre)
                }
            }
        }

        Button(onClick = { showDropdown = true }) {
            Text("Выбрать жанр")
        }
        Text(text = selectedGenre)

        Button(onClick = { onCreatePlaylist(playlistName, imageUri, selectedGenre) }) {
            Text("Создать плейлист")
        }
    }
}
