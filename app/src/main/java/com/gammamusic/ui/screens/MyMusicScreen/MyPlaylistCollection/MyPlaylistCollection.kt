package com.example.freelis.ui.screens.MyMusicScreen.MyPlaylistCollection



import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.size

import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter



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
Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize().background(
    brush = Brush.linearGradient(
        colors = listOf(Color(0xFF000000), Color(0xFFE8117C)), start = Offset.Zero, end = Offset(0f, 800f)
    )

    )) {
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

}
