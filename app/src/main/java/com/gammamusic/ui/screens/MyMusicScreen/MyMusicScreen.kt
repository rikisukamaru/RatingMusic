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
import androidx.compose.foundation.layout.wrapContentSize

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

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton

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
import androidx.compose.ui.draw.clip

import androidx.compose.ui.geometry.Offset

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
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Top
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.rmlogo),
                            contentDescription = "",
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)

                        )
                        Text(
                            text = "Ваша библиотека",
                            style = TextStyle(
                                fontSize = 24.sp,
                                lineHeight = 29.sp,
                                fontFamily = FontFamily(Font(R.font.inter_bold)),
                                fontWeight = FontWeight(700),
                                color = Color(0xFFFFFFFF),
                                shadow = Shadow(
                                    color = Color(0xFFE80B7C),
                                    offset = Offset(0f, 10f),
                                    blurRadius = 6f
                                )
                            ), modifier = Modifier.padding(top=5.dp)
                        )
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

            )
            LazyColumn(Modifier.fillMaxSize()) {
                items(searchState) { item: Search ->
                    Card(
                        onClick = {
                            isPlayerVisible = true
                            trackId = item.id
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                            .height(40.dp)
                            .background(Color.White)
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
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Добавить в мою коллекцию"
                                )
                            }

                        }

                    }

                }
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { androidx.compose.material.Text("Мой чарт") },
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    actions = {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            backgroundColor = Color.Black,
                            contentColor = Color(0xFFE80B7C),

                        ) {
                            Tab(
                                text = { androidx.compose.material.Text("Музыка", style = (androidx.compose.material3.MaterialTheme.typography.bodyLarge).copy(fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                                    fontWeight = FontWeight(1000),
                                    letterSpacing = 0.96.sp,
                                    textAlign = TextAlign.Center,
                                    shadow = Shadow(
                                        color = Color(0xFFE80B7C),
                                        offset = Offset(2f, 2f),
                                        blurRadius = 10f
                                    )

                                        ))},
                                selected = selectedTab == 0,
                                onClick = { setSelectedTab(0) },
                                selectedContentColor = Color(0xFFE80B7C),
                                unselectedContentColor = Color.White

                            )
                            Tab(
                                text = { androidx.compose.material.Text("Плейлисты",style = (androidx.compose.material3.MaterialTheme.typography.bodyLarge).copy(fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                                    fontWeight = FontWeight(1000),
                                    letterSpacing = 0.96.sp,
                                    textAlign = TextAlign.Center,
                                    shadow = Shadow(
                                        color = Color(0xFFE80B7C),
                                        offset = Offset(2f, 2f),
                                        blurRadius = 10f
                                    )
                                ))},
                                selected = selectedTab == 1,
                                selectedContentColor = Color(0xFFE80B7C),
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
fun MyMus(viewModel: MyMusicCollectionViewModel){
    val searches by viewModel.searches.observeAsState(emptyList())
    var isPlayerVisible by remember { mutableStateOf(false) }

    var trackId by remember{
        mutableStateOf(0L)
    }

        LazyColumn (Modifier.background(Color.Black)){
            itemsIndexed(searches) { index, search ->
                androidx.compose.material. Card(
                    onClick = {
                        isPlayerVisible=true
                        trackId = search.id
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .background(color = Color(0xCF000000)),
                    elevation = 10.dp,
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
                            painter = rememberAsyncImagePainter(model = search.album.cover_medium ?: "https://shutniks.com/wp-content/uploads/2020/01/unnamed-2.jpg"),
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
                                    fontSize = 17.sp,
                                    lineHeight = 21.sp,
                                    fontFamily = FontFamily(Font(R.font.inter_bold)),
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
                                    fontSize = 14.sp,
                                    lineHeight = 17.sp,
                                    fontFamily = FontFamily(Font(R.font.inter_bold)),
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
                    .padding(horizontal = 16.dp)
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
                                        fontSize = 21.sp,
                                        lineHeight = 17.sp,
                                        fontFamily = FontFamily(Font(R.font.inter_bold)),
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.02.sp,
                                        color = Color.White
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = playlist.genre!!,
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        lineHeight = 17.sp,
                                        fontFamily = FontFamily(Font(R.font.inter_bold)),
                                        fontWeight = FontWeight.Bold,
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










