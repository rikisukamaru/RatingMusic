package com.example.freelis.ui.screens.MyMusicScreen.MyPlaylistCollection



import android.net.Uri

import android.widget.Toast

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape


import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.gammamusic.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistForm(onCreatePlaylist: (String, Uri?, String) -> Unit) {
    var playlistName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedGenre by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val genres = listOf("rap", "hip-hop", "alternative", "pop", "r&b", "rock", "classic", "indi", "techno")

    // Запрос на выбор изображения
    val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxWidth().fillMaxHeight(0.8f)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF000000), Color(0xFFE8117C)),
                    start = Offset.Zero,
                    end = Offset(0f, 900f)
                )
            )
    ) {
        Column {
            TextField(
                value = playlistName,
                onValueChange = { playlistName = it },
                label = {
                    Text(
                        "Введите название...",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 21.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.02.sp,
                            color = Color.White
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent,
                    textColor = Color.White
                ),
                modifier = Modifier.padding(start = 16.dp),
                textStyle = TextStyle(
                    fontSize = 21.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.02.sp,
                    color = Color.White
                ),
            )

            TextButton(
                onClick = { pickImageLauncher.launch("image/*") },
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    "Выбрать изображение",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 21.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.02.sp,
                        color = Color.White
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(model = uri),
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(start = 25.dp)
                        .clip(RoundedCornerShape(15.dp))
                )
            }

            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false }
            ) {
                genres.forEach { genre ->
                    DropdownMenuItem(onClick = {
                        selectedGenre = genre
                        showDropdown = false
                    }) {
                        Text(
                            text = genre,
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 21.sp,
                                fontFamily = FontFamily(Font(R.font.inter_bold)),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.02.sp,
                                color = Color.Black
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            TextButton(onClick = { showDropdown = true }) {
                Text(
                    "Выбрать жанр",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 21.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.02.sp,
                        color = Color.White
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Text(
                text = selectedGenre,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.02.sp,
                    color = Color.White
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 16.dp)
            )

            Box(
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable {
                        if (playlistName.isNotBlank() && imageUri != null && selectedGenre.isNotBlank()) {
                            onCreatePlaylist(playlistName, imageUri, selectedGenre)
                        } else {
                            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = R.drawable.whitebutton),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        "Создать плейлист",
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 21.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.02.sp,
                            color = Color.Black
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,

                    )
                }
            }
        }
    }
}
