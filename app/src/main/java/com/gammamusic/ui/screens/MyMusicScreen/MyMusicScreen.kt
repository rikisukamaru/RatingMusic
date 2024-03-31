package com.gammamusic.ui.screens.MyMusicScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.freelis.ui.screens.MyMusicScreen.MyMusicViewModel
import com.gammamusic.R

import com.gammamusic.domain.model.Search.Search

import com.gammamusic.ui.navigation.MainNavigation.ScreensInMyMusic
import com.gammamusic.ui.screens.MusicPlayer.MusicPlayerScreen



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

    var isSearchExpanded by remember { mutableStateOf(false) }


    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xCF000000))
    ) {
        if (!isSearchExpanded) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround,

                ) {
                Box(Modifier.background(Color(0xCF1E1E1E))) {
                    Row(
                        Modifier
                            .background(Color.Black)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Image(
                            painter = painterResource(id = R.drawable.musium_logo),
                            contentDescription = "",
                            modifier = Modifier
                                .width(63.dp)
                                .height(48.dp)
                                .padding(end = 15.dp, start = 10.dp)
                        )
                        Text(
                            text = "Your Music",
                            style = TextStyle(
                                fontSize = 27.sp,
                                lineHeight = 33.sp,
                                fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 2.4299999999999997.sp,
                                color = Color(0xFF00C2CB)
                            ), modifier = Modifier.padding(end = 121.dp)
                        )
                        IconButton(onClick = { isSearchExpanded = true }) {
                            Image(painter = painterResource(id = R.drawable.search), contentDescription = "Search Icon",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        }
                    }
                }
            }
        } else {
            TextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    viewModel.searchQuery.value = newText
                    if (newText.isBlank()) {
                        viewModel.clearSearchResults()
                    } else {
                        viewModel.search(newText)
                    } },
                label = { Text("Search") },
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.search), contentDescription = "Search Icon", modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear Search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()

            )
            LazyColumn() {
                items(searchState) { item: Search ->
                    Card(onClick = { isPlayerVisible = true
                        trackId = item.id},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                            .height(40.dp)
                            .background(Color.White)
                    ) {Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = item.album.cover_medium ?:"https://shutniks.com/wp-content/uploads/2020/01/unnamed-2.jpg"),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Crop,            // crop the image if it's not a square
                            modifier = Modifier
                                .size(40.dp)
                                .wrapContentSize()   // add a border (optional)
                        )

                        Column {
                            Text(text = item.title)
                            Text(text = item.artist.name)
                        }

                        IconButton(onClick = { viewModel.addSearchToUserCollection(item) }) {
                            Icon(Icons.Filled.Add, contentDescription = "Добавить в мою коллекцию")
                        }

                    }

                    }

                }
            }
        }
        Column {
            Card(modifier = Modifier
                .background(Color.Black)
                .height(56.dp)
                .fillMaxWidth()
                .padding(end = 24.dp, start = 24.dp), colors = CardDefaults.cardColors(
                containerColor = Color.Black // Установка черного цвета фона
            )) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = {  navController.navigate(ScreensInMyMusic.MyPlaylistCollection.route){

                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                    } }) {
                        Image(painter = painterResource(id = R.drawable.ellipse), contentDescription = "", modifier = Modifier.size(56.dp))
                        Image(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "",
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                                .padding(2.dp)
                        )
                    }

                    Text(
                        text = "Ваши плейлисты",
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 25.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 1.2.sp,
                            color = Color(0xFFFFFFFF)
                        ),
                        modifier = Modifier.padding(start = 24.dp)
                    )

                }

            }

            Card(modifier = Modifier
                .background(Color.Black)
                .height(56.dp)
                .fillMaxWidth()
                .padding(end = 24.dp, start = 24.dp), colors = CardDefaults.cardColors(
                containerColor = Color.Black // Установка черного цвета фона
            )) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = {navController.navigate(ScreensInMyMusic.MyMusicCollection.route){

                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                    } }) {
                        Image(painter = painterResource(id = R.drawable.ellipse), contentDescription = "", modifier = Modifier.size(56.dp))
                        Image(
                            painter = painterResource(id = R.drawable.love),
                            contentDescription = "",
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                                .padding(2.dp)
                        )
                    }

                    Text(
                        text = "Ваши вкусы в музыке",
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 25.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 1.2.sp,
                            color = Color(0xFFFFFFFF)
                        ),
                        modifier = Modifier.padding(start = 24.dp)
                    )

                }

            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth().background(Color.Black)) {
                Image(painter = painterResource(id = R.drawable.sort), contentDescription = "",modifier = Modifier
                    .padding(start = 25.dp)
                    .width(17.dp)
                    .height(13.dp))

                Text(
                    text = "Recently played",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                        fontWeight = FontWeight(700),
                        letterSpacing = 0.96.sp,
                        color = Color(0xFF39C0D4)
                    ), modifier = Modifier.padding(start = 5.dp)
                )
            }
        }
    }

    if (isPlayerVisible) {
        MusicPlayerScreen(id = trackId)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi =  true)
@Composable
fun scr() {
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xCF000000))
    ) {
        if (!isSearchExpanded) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround,

            ) {
                Box(Modifier.background(Color(0xCF1E1E1E))) {
                    Row(
                        Modifier
                            .background(Color.Black)
                            .fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.Start) {
                        Image(
                            painter = painterResource(id = R.drawable.musium_logo),
                            contentDescription = "",
                            modifier = Modifier
                                .width(63.dp)
                                .height(48.dp)
                                .padding(end = 15.dp, start = 10.dp)
                        )
                        Text(
                            text = "Your Music",
                            style = TextStyle(
                                fontSize = 27.sp,
                                lineHeight = 33.sp,
                                fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 2.4299999999999997.sp,
                                color = Color(0xFF00C2CB)
                            ), modifier = Modifier.padding(end = 121.dp)
                        )
                        IconButton(onClick = { isSearchExpanded = true }) {
                            Image(painter = painterResource(id = R.drawable.search), contentDescription = "Search Icon",
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(20.dp)
                                    )
                        }
                    }
                }
            }
        } else {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search") },
                leadingIcon = { Icon(painter = painterResource(id = R.drawable.search), contentDescription = "Search Icon", modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear Search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()

            )
        }
        Column {
            Card(modifier = Modifier
                .background(Color.Black)
                .height(56.dp)
                .fillMaxWidth()
                .padding(end = 24.dp, start = 24.dp), colors = CardDefaults.cardColors(
                containerColor = Color.Black // Установка черного цвета фона
            )) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = { /*TODO*/ }) {
                       Image(painter = painterResource(id = R.drawable.ellipse), contentDescription = "", modifier = Modifier.size(56.dp))
                        Image(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "",
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                                .padding(2.dp)
                        )
                    }

                    Text(
                        text = "Ваши плейлисты",
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 25.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 1.2.sp,
                            color = Color(0xFFFFFFFF)
                        ),
                        modifier = Modifier.padding(start = 24.dp)
                    )

                }

            }

            Card(modifier = Modifier
                .background(Color.Black)
                .height(56.dp)
                .fillMaxWidth()
                .padding(end = 24.dp, start = 24.dp), colors = CardDefaults.cardColors(
                containerColor = Color.Black // Установка черного цвета фона
            )) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(painter = painterResource(id = R.drawable.ellipse), contentDescription = "", modifier = Modifier.size(56.dp))
                        Image(
                            painter = painterResource(id = R.drawable.love),
                            contentDescription = "",
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                                .padding(2.dp)
                        )
                    }

                    Text(
                        text = "Ваши вкусы в музыке",
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 25.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 1.2.sp,
                            color = Color(0xFFFFFFFF)
                        ),
                        modifier = Modifier.padding(start = 24.dp)
                    )

                }
            }
        }


        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth().background(Color.Black)) {
            Image(painter = painterResource(id = R.drawable.sort), contentDescription = "",modifier = Modifier
                .padding(start = 25.dp)
                .width(17.dp)
                .height(13.dp))

            Text(
                text = "Recently played",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                    fontWeight = FontWeight(700),
                    letterSpacing = 0.96.sp,
                    color = Color(0xFF39C0D4)
                ), modifier = Modifier.padding(start = 5.dp)
            )
        }



    }

}







