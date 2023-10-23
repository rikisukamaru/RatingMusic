package com.gammamusic.ui.screens.MyMusicScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.freelis.ui.screens.MyMusicScreen.MyMusicViewModel
import com.gammamusic.domain.model.Player.Track
import com.gammamusic.domain.model.Search.Search
import com.gammamusic.ui.navigation.MainNavigation.ScreensInMyMusic
import com.gammamusic.ui.screens.MusicPlayer.MusicPlayerScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMusicScreen(
    navController: NavController,
    viewModel: MyMusicViewModel =  androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var searchText by remember { mutableStateOf("") }
    var isPlayerVisible by remember { mutableStateOf(false) }
    var trackId by remember{
        mutableStateOf(0L)
    }
    val searchState by remember { viewModel.searchLiveData }.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        //Апи на поиск по артисту
        Column {
            TextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    viewModel.searchQuery.value = newText
                    if (newText.isBlank()) {
                        viewModel.clearSearchResults()
                    } else {
                        viewModel.search(newText)
                    }
                },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn {
                items(searchState) { item: Search ->
                    Card(onClick = { isPlayerVisible = true
                                   trackId = item.id},
                         modifier = Modifier
                             .fillMaxWidth()
                             .padding(bottom = 5.dp)
                             .height(30.dp)
                             .background(Color.White)
                    ) {
                        Text(text = item.title)

                    }

                }
            }
        }




        Button(
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(Color.White),
            modifier = Modifier.padding(5.dp),
            onClick = {
                navController.navigate(ScreensInMyMusic.MyMusicCollection.route)
            }
        ) {
            Text(
                text = "Моя музыкальная коллекция",
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                color = Color.Black
            )

        }
        Button(
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(Color.White),
            modifier = Modifier.padding(5.dp),
            onClick = {
                navController.navigate(ScreensInMyMusic.MyPlaylistCollection.route)
            }
        ) {
            Text(
                text = "Мои плейлисты",
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                color = Color.Black
            )

        }

    }
    if (isPlayerVisible) {
        MusicPlayerScreen(id = trackId)
    }
}


