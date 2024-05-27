@file:OptIn(ExperimentalMaterialApi::class)

package com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.PlayList

import android.util.Log

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Check

import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.ripple.rememberRipple


import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api

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

import androidx.compose.ui.input.nestedscroll.nestedScroll

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter
import com.gammamusic.R
import com.gammamusic.domain.model.Player.Track

import com.gammamusic.ui.screens.MusicPlayer.MusicPlayerScreen

import com.google.firebase.auth.FirebaseAuth


import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PlayListScreen(viewModel: PlayListViewModel,navController: NavController,  selectedPlaylistId: String) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val isPlayerVisible by remember { mutableStateOf(false) }

    val playlist by viewModel.playlist.observeAsState()
    val tracks by viewModel.trakes.observeAsState(initial = emptyList())
    val trackId by remember{
        mutableStateOf(0L)
    }
    LaunchedEffect(key1 = selectedPlaylistId) {
        viewModel.loadPlayList(playlistId = selectedPlaylistId)
    }



    if (playlist == null) {
        androidx.compose.material3.Text("Загрузка плейлиста...")
    } else if (tracks.isEmpty()) {
        androidx.compose.material3.Text("Треклист пуст")
    } else {
        // Отображение плейлиста и треклиста
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)){
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navController.navigate("MyMusic") }) {
                    Image(painter = painterResource(id = R.drawable.left_md), contentDescription = "BAck Icon",
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                }
                androidx.compose.material3.Text(
                    text = playlist!!.name?: "Неизвестный плейлист",
                    style = TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 15.sp,
                        fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                        fontWeight = FontWeight(500),
                        letterSpacing = 0.72.sp,
                        color = Color(0xFFFFFFFF)
                    )
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Image(painter = painterResource(id = R.drawable.option), contentDescription = "BAck Icon",
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround,modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)) {
                Image(painter = rememberAsyncImagePainter(model = playlist!!.photoUrl), contentDescription = "", modifier = Modifier
                    .width(263.dp)
                    .height(252.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround,modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 21.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable {
                            if (!playlist!!.published) {
                                viewModel.publishSelectedPlaylist(selectedPlaylistId)
                            }
                        }
                ) {
                    if (!playlist!!.published) {
                        Image(
                            painter = rememberAsyncImagePainter(model = R.drawable.redbutton),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.15f)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.material3.Text(
                            text = if (playlist!!.published) "Опубликовано" else "Опубликовать",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.codenext_book)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 0.8.sp,
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        )
                        if (playlist!!.published) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check Icon",
                                tint = Color(0xFFE91E63),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
                    androidx.compose.material3.Text(
                        text = playlist?.genre!!,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.codenext_book)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 0.78.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xB3FFFFFF)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(Modifier.padding(bottom = 65.dp).nestedScroll(
                rememberNestedScrollInteropConnection()
            )) {
                items(tracks.size) { index ->
                    val track = tracks[index]
                    TrackItem(track = track,viewModel, playlistId = selectedPlaylistId)
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 100.dp), contentAlignment = Alignment.BottomEnd) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { scope.launch { sheetState.show() } }) {
            Image(painter = rememberAsyncImagePainter(model = R.drawable.ellipse_red), contentDescription ="", modifier = Modifier.size(150.dp) )
            androidx.compose.material.Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.plus),
                tint = Color.White,
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
        }

    }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp) // Задайте желаемую высоту листа
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ){
               PlayListCreate(viewModel,selectedPlaylistId)
            }
        },
        sheetShape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
    ) {
    }
    if (isPlayerVisible) {
        MusicPlayerScreen(id =trackId)
    }

}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun TrackItem(track: Track, viewModel: PlayListViewModel, playlistId: String) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        val userId = currentUser.uid


        val isPlayerVisible by remember { mutableStateOf(false) }
        val trackId by remember { mutableStateOf(0L) }
        val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()


        val offsetX = remember { Animatable(0f) }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .background(color = Color(0xFF000000))

        ) {
            // Ваш контент карточки
            androidx.compose.material.Card(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { scope.launch { sheetState.show() } },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple()
                    )
                    .background(color = Color(0xFF000000))
                    .offset {
                        IntOffset(
                            offsetX.value.roundToInt(),
                            0
                        )
                    },
                elevation = 10.dp,
                backgroundColor = Color(0xFF000000)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(horizontalArrangement = Arrangement.Start) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = track.cover ?: "https://shutniks.com/wp-content/uploads/2020/01/unnamed-2.jpg"
                            ),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(53.dp)
                                .wrapContentSize()
                        )
                        Column(Modifier.padding(start = 13.dp)) {
                            androidx.compose.material3.Text(
                                text = track.title,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                                    fontWeight = FontWeight(700),
                                    letterSpacing = 0.96.sp,
                                    color = Color(0xFFFFFFFF)
                                )
                            )
                            androidx.compose.material3.Text(
                                text = track.nameArtist,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    lineHeight = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_book)),
                                    fontWeight = FontWeight(700),
                                    letterSpacing = 0.6000000000000001.sp,
                                    color = Color(0xFF8A9A9D)
                                )
                            )
                        }
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(id = R.drawable.option),
                            contentDescription = "Option Icon",
                            modifier = Modifier
                                .width(15.dp)
                                .height(15.dp)
                        )
                    }
                }
            }

            // Отображение иконок в зависимости от направления свайпа

        }

        if (isPlayerVisible) {
            MusicPlayerScreen(id = trackId)
        }
    } else {
        Log.e("TAG", "User not logged in")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListCreate(viewModel: PlayListViewModel, selectedPlaylistId: String){
    var trackId by remember {
        mutableStateOf(0L)
    }
    val searches by viewModel.searches.observeAsState(emptyList())

    LazyColumn(modifier = Modifier.wrapContentSize(Alignment.TopCenter)) {
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
                    Box(modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .wrapContentWidth(Alignment.Start)) {
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
