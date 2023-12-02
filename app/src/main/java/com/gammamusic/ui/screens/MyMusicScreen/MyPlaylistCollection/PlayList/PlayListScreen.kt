package com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.PlayList

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListScreen(viewModel: PlayListViewModel, selectedPlaylistId: String) {

    val searches by viewModel.searches.observeAsState(emptyList())

    var trackId by remember {
        mutableStateOf(0L)
    }

            LazyColumn(modifier = Modifier.padding(bottom = 150.dp)) {
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
                            Box(modifier = Modifier.fillMaxWidth(0.4f).wrapContentWidth(Alignment.Start)) {
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
