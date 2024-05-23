package com.gammamusic.ui.screens.RatingScreen.PublishedPlayList

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable


import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState

import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll

import androidx.compose.ui.input.pointer.pointerInput
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

@Composable
fun pbPlayList(navController: NavController, viewModel: pbPlayListViewModel, playlistId: String) {
    val playlist by viewModel.playlist.observeAsState()
    val tracks by viewModel.trakes.observeAsState(initial = emptyList())

    LaunchedEffect(key1 = playlistId) {
        viewModel.loadPlayList(playlistId)
    }

    // Отображение состояния загрузки
    if (playlist == null) {
        Text("Загрузка плейлиста...")
    } else if (tracks.isEmpty()) {
        Text("Треклист пуст")
    } else {
        // Отображение плейлиста и треклиста
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)){
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navController.navigate("Rating") }) {
                    Image(painter = painterResource(id = R.drawable.left_md), contentDescription = "BAck Icon",
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                }
                Text(
                    text = "FROM TOP #110",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 15.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
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
                .padding(top = 36.dp)) {
                Image(painter = rememberAsyncImagePainter(model = playlist!!.photoUrl), contentDescription = "", modifier = Modifier
                    .width(263.dp)
                    .height(252.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround,modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 21.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = playlist?.name ?: "Неизвестный плейлист",
                        style = TextStyle(
                            fontSize = 34.sp,
                            lineHeight = 42.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 3.06.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFFFFFFF)
                        )
                    )
                    Text(
                        text = playlist?.genre!!,
                        style = TextStyle(
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 0.78.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xB3FFFFFF)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(Modifier.padding(bottom = 65.dp).nestedScroll(rememberNestedScrollInteropConnection())) {
                items(tracks.size) { index ->
                    val track = tracks[index]
                    TrackItem(track = track,viewModel, playlistId = playlistId)
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun TrackItem(track: Track, viewModel: pbPlayListViewModel, playlistId: String) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        val userId = currentUser.uid
        LaunchedEffect(userId) {
            viewModel.loadSwipeCount(playlistId, userId)
        }

        val isPlayerVisible by remember { mutableStateOf(false) }
        val trackId by remember { mutableStateOf(0L) }
        val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        val coroutineScope = rememberCoroutineScope()
        val offsetX = remember { Animatable(0f) }
        val threshold = 100 // Минимальное расстояние для распознавания свайпа

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .background(color = Color(0xFF000000))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount)
                            }
                        },
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetX.value > threshold) {
                                    viewModel.updatePlaylistRating(playlistId, 25)
                                    viewModel.increaseSwipeCount(playlistId, userId)
                                } else if (offsetX.value < -threshold) {
                                    viewModel.updatePlaylistRating(playlistId, -25)
                                    viewModel.increaseSwipeCount(playlistId, userId)
                                }
                                offsetX.animateTo(targetValue = 0f)
                            }
                        },
                        onDragCancel = {
                            coroutineScope.launch {
                                offsetX.animateTo(targetValue = 0f)
                            }
                        }
                    )
                }
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
                            Text(
                                text = track.title,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                                    fontWeight = FontWeight(700),
                                    letterSpacing = 0.96.sp,
                                    color = Color(0xFFFFFFFF)
                                )
                            )
                            Text(
                                text = track.nameArtist,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    lineHeight = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.raleway_extralight)),
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
            AnimatedVisibility(visible = offsetX.value > 0, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = "+25",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                        fontWeight = FontWeight(700),
                        letterSpacing = 0.96.sp,
                        color = Color(0xFFFFFFFF)
                    )
                )
            }
            AnimatedVisibility(visible = offsetX.value < 0, modifier = Modifier.align(Alignment.CenterEnd)) {
                Text(
                    text = "-25",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                        fontWeight = FontWeight(700),
                        letterSpacing = 0.96.sp,
                        color = Color(0xFFFFFFFF)
                    )
                )
            }
        }
        if (sheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = { scope.launch { sheetState.hide() } }
            ) {
                // Содержимое модального щита
            }
        }
        if (isPlayerVisible) {
            MusicPlayerScreen(id = trackId)
        }
    } else {
        Log.e("TAG", "User not logged in")
    }
}





