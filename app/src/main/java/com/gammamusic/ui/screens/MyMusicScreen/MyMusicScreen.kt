package com.gammamusic.ui.screens.MyMusicScreen
import android.annotation.SuppressLint

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.freelis.ui.screens.MyMusicScreen.MyMusicViewModel
import com.example.freelis.ui.screens.MyMusicScreen.MyPlaylistCollection.PlaylistForm

import com.gammamusic.R

import com.gammamusic.domain.model.Search.Search


import com.gammamusic.ui.screens.MusicPlayer.MusicPlayerScreen
import com.gammamusic.ui.screens.MyMusicScreen.MyMusicCollection.MyMusicCollectionViewModel
import com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.MyPlaylistCollectionViewModel
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMusicScreen(
    navController: NavController,
    viewModel: MyMusicViewModel =  viewModel()
) {
    val vviewmodel: MyMusicCollectionViewModel = viewModel()
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(0) }

    var searchText by remember { mutableStateOf("") }
    var isPlayerVisible by remember { mutableStateOf(false) }
    var trackId by remember {
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

                    Row(
                        Modifier
                            .background(Color.Black)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top
                    ) {
                        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 10.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.rmlogo),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp)

                            )
                            Text(
                                text = "Библиотека",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    lineHeight = 29.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                                    fontWeight = FontWeight(700),
                                    color = Color(0xFFFFFFFF),

                                ), modifier = Modifier.padding(top=5.dp)
                            )
                        }

                        IconButton(onClick = { isSearchExpanded = true }) {
                            Image(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "Search Icon",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)



                            )
                        }

                    }

        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF5D1134), Color(0xFF14140E)),
                            start = Offset.Zero,
                            end = Offset(0f, 500f)
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    // Search bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.rmlogo),
                            contentDescription = "",
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)

                        )
                        Text(
                            text = "Поиск",
                            style = TextStyle(
                                fontSize = 24.sp,
                                lineHeight = 29.sp,
                                fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                                fontWeight = FontWeight(700),
                                color = Color(0xFFFFFFFF),
                                shadow = Shadow(
                                    color = Color(0xFFE80B7C),
                                    offset = Offset(0f, 10f),
                                    blurRadius = 6f
                                )
                            )
                        )
                    }

                    // Search field
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
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.02.sp
                        ),
                        label = { Text("Песни,Артисты и Жанры", style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 21.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.02.sp,
                            color = Color.White
                        ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "Search Icon",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchText.isNotEmpty()) {
                                IconButton(onClick = { searchText = "" }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Clear Search")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFFD9D9D9)),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFFD9D9D9),
                            textColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    if (searchText.isNotBlank()) {
                        LazyColumn(
                            Modifier
                                .fillMaxSize()
                                .padding(bottom = 70.dp)) {
                            items(searchState) { item: Search ->
                                Card(
                                    onClick = {
                                        trackId = item.id
                                        isPlayerVisible = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 5.dp)
                                        .height(80.dp) // Adjusted height for better display
                                        ,
                                    colors= CardDefaults.cardColors(containerColor = Color.Transparent)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                model = item.album.cover_medium
                                                    ?: "https://shutniks.com/wp-content/uploads/2020/01/unnamed-2.jpg"
                                            ),
                                            contentDescription = "avatar",
                                            contentScale = ContentScale.Crop, // crop the image if it's not a square
                                            modifier = Modifier
                                                .size(80.dp) // Adjusted size for better display
                                                .clip(RoundedCornerShape(8.dp))
                                        )

                                        Column(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .weight(1f) // Ensures the text takes available space
                                        ) {
                                            Text(
                                                text = item.title,
                                                style = TextStyle(
                                                    fontSize = 16.sp,
                                                    lineHeight = 21.sp,
                                                    fontFamily = FontFamily(Font(R.font.codenext_book)),
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 1.02.sp,
                                                    color = Color.White
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Text(
                                                text = item.artist.name,
                                                style = TextStyle(
                                                    fontSize = 16.sp,
                                                    lineHeight = 21.sp,
                                                    fontFamily = FontFamily(Font(R.font.codenext_book)),
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 1.02.sp,
                                                    color = Color.White
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        IconButton(onClick = {
                                            viewModel.addSearchToUserCollection(item)
                                        },modifier = Modifier.align(Alignment.CenterVertically)) {
                                            Icon(
                                                Icons.Filled.Add,
                                                tint = Color.White,
                                                contentDescription = "Добавить в мою коллекцию"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else{

                            Text(
                                text = "Твои любимые жанры",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    lineHeight = 21.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.02.sp,
                                    color = Color.White
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 12.dp, top = 16.dp)
                            )
                            Image(painter = rememberAsyncImagePainter(model = R.drawable.top), contentDescription = "",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxHeight(0.42f)
                                    .fillMaxWidth()
                                    .padding(top = 17.dp))

                            Text(
                                text = "Все подборки",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    lineHeight = 21.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.02.sp,
                                    color = Color.White
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 12.dp, top = 10.dp)
                            )

                            Image(painter = rememberAsyncImagePainter(model = R.drawable.group), contentDescription = "",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxHeight(0.79f)
                                    .fillMaxWidth()
                                    )
                        }


                }
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { androidx.compose.material.Text("Моя библиотека") },
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    actions = {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            backgroundColor = Color.Black,
                            contentColor = Color(0xFFE91E63),

                        ) {
                            Tab(
                                text = { androidx.compose.material.Text("Музыка", style = (androidx.compose.material3.MaterialTheme.typography.bodyLarge).copy(fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                                    fontWeight = FontWeight(1000),
                                    letterSpacing = 0.96.sp,
                                    textAlign = TextAlign.Center,


                                        ))},
                                selected = selectedTab == 0,

                                onClick = { setSelectedTab(0) },

                                unselectedContentColor = Color.White

                            )
                            Tab(
                                text = { androidx.compose.material.Text("Плейлисты",style = (androidx.compose.material3.MaterialTheme.typography.bodyLarge).copy(fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                                    fontWeight = FontWeight(1000),
                                    letterSpacing = 0.96.sp,
                                    textAlign = TextAlign.Center,

                                ))},
                                selected = selectedTab == 1,
                                unselectedContentColor = Color.White,
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
                    .background(Color(0xCF000000))
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> MyMus(vviewmodel)
                    1 -> myPl(navController = navController)

                }
            }
        }




    }
    if (isPlayerVisible) {
        MusicPlayerScreen(id = trackId)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MyMus(viewModel: MyMusicCollectionViewModel) {
    val searches by viewModel.searches.observeAsState(emptyList())
    var isPlayerVisible by remember { mutableStateOf(false) }

    var trackId by remember {
        mutableStateOf(0L)
    }
    Box(Modifier.fillMaxSize().background(Color.Black)) {


        LazyColumn(
            Modifier
                .background(color = Color(0xCF000000))
                .padding(bottom = 65.dp)
        ) {
            itemsIndexed(searches) { index, search ->
                androidx.compose.material.Card(
                    onClick = {
                        isPlayerVisible = true
                        trackId = search.id
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),

                    backgroundColor = Color(0xCF000000)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = search.album.cover_medium
                                    ?: "https://shutniks.com/wp-content/uploads/2020/01/unnamed-2.jpg"
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(62.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = search.title,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 21.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_book)),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.02.sp,
                                    color = Color.White
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = search.artist.name,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    lineHeight = 17.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_book)),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.7.sp,
                                    color = Color(0xFF8A9A9D)
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        IconButton(
                            onClick = { viewModel.deleteSongFromCollection(search.id) },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                }

            }
        }
    }

        if (isPlayerVisible) {
            MusicPlayerScreen(id = trackId)
        }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun myPl(navController: NavController){
    val viewModel: MyPlaylistCollectionViewModel = viewModel()
    val playlists by viewModel.playlists.observeAsState(listOf())
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadPlaylists()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {

                PlaylistForm(viewModel::createPlaylist)

        }
    ) {     Box(modifier = Modifier.fillMaxSize()) {


        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .fillMaxHeight()
                    .padding(bottom = 65.dp)
            ) {
                itemsIndexed(playlists) { index, playlist ->
                    androidx.compose.material.Card(
                        onClick = {
                            navController.navigate("PlayList/${playlist.id}")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .fillMaxHeight(0.3f)
                            .background(Color.Black),
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = playlist.photoUrl),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(104.dp) // Увеличиваем размер обложки
                                    .clip(RoundedCornerShape(8.dp)) // Закругляем углы изображения
                                    .background(Color.Black)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = playlist.name!!,
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        lineHeight = 17.sp,
                                        fontFamily = FontFamily(Font(R.font.codenext_book)),
                                        fontWeight = FontWeight.ExtraLight,
                                        letterSpacing = 1.02.sp,
                                        color = Color.White
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = playlist.genre!!,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        lineHeight = 17.sp,
                                        fontFamily = FontFamily(Font(R.font.codenext_book)),
                                        fontWeight = FontWeight.Light,
                                        letterSpacing = 1.02.sp,
                                        color = Color(0xFF6E7172)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                        }
                    }
                }
             }


            }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp), contentAlignment = Alignment.BottomEnd) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { coroutineScope.launch { sheetState.show() } }) {
                Image(painter = rememberAsyncImagePainter(model = R.drawable.ellipse_red), contentDescription ="", modifier = Modifier.size(150.dp) )
                Icon(painter = rememberAsyncImagePainter(model = R.drawable.plus), tint = Color.White, contentDescription ="", modifier = Modifier.size(24.dp) )
            }

        }

        }
    }
}










