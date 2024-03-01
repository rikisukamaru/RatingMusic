package com.example.freelis.ui.screens.RatingScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.gammamusic.domain.model.Playlist
import com.gammamusic.ui.screens.RatingScreen.PlaylistChartViewModel

@Composable
fun RatingScreen() {
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(0) }
    val playlistViewModel: PlaylistChartViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мой чарт") },
                backgroundColor = Color.Black,
                contentColor = Color.White,
                actions = {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    ) {
                        Tab(
                            text = { Text("Рейтинг пользователей") },
                            selected = selectedTab == 0,
                            onClick = { setSelectedTab(0) }
                        )
                        Tab(
                            text = { Text("Чарт плейлистов") },
                            selected = selectedTab == 1,
                            onClick = { setSelectedTab(1) }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> Text("Рейтинг пользователей")
                1 -> PlaylistChart(playlistViewModel.playlists.value)
            }
        }
    }
}

@Composable
fun PlaylistChart(playlists: List<Playlist>) {
    LazyColumn {
        items(playlists) { playlist ->
            PlaylistCard(playlist)
        }
    }
}

@Composable
fun PlaylistCard(playlist: Playlist) {
    Card(elevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(playlist.photoUrl ?: ""),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(text = playlist.name ?: "Без названия", style = MaterialTheme.typography.h6)
                Text(text = "Рейтинг: ${playlist.rating}")
            }
        }
    }
}

