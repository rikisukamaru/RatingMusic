package com.example.freelis.ui.screens.RatingScreen



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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*

import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip


import androidx.compose.ui.graphics.Color

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign


import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter


import com.gammamusic.R
import com.gammamusic.domain.model.Playlist
import com.gammamusic.domain.model.Rating.User
import com.gammamusic.ui.screens.RatingScreen.PlaylistChartViewModel
import com.gammamusic.ui.screens.RatingScreen.PublishedPlayList.UserChatViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun RatingScreen(navController: NavController) {
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(0) }
    val playlistViewModel: PlaylistChartViewModel = viewModel()
    val usersViewModel: UserChatViewModel = viewModel()
    LaunchedEffect(Unit) {
        usersViewModel.loadTopUsers()
        playlistViewModel.loadPlaylists()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()) {

            Row(
                Modifier
                    .background(Color.Black)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rmlogo),
                        contentDescription = "",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)

                    )
                    androidx.compose.material3.Text(
                        text = "Рейтинг",
                        style = TextStyle(
                            fontSize = 24.sp,
                            lineHeight = 29.sp,
                            fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),

                            ), modifier = Modifier.padding(top = 5.dp)
                    )
                }

                androidx.compose.material3.IconButton(onClick = { }) {
                    Image(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search Icon",
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)


                    )
                }

            }


            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { androidx.compose.material.Text("Рейтинг") },
                        backgroundColor = Color.Black,
                        contentColor = Color.White,
                        actions = {
                            TabRow(
                                selectedTabIndex = selectedTab,
                                backgroundColor = Color.Black,
                                contentColor = Color(0xFFE91E63),

                                ) {
                                Tab(
                                    text = { androidx.compose.material.Text("Авторы", style = (androidx.compose.material3.MaterialTheme.typography.bodyLarge).copy(fontSize = 16.sp,
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
                ){
                    when (selectedTab) {
                        0 -> UserRatingChart(userChatViewModel = usersViewModel)
                        1 -> PlaylistChart(playlistViewModel.playlists.value.sortedByDescending { it.rating }, navController = navController, viewModel = playlistViewModel)
                    }
                }
            }

    }

}

@Composable
fun UserRatingChart(userChatViewModel: UserChatViewModel) {
    val users by userChatViewModel.users.observeAsState(initial = emptyList())
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        itemsIndexed(users.sortedByDescending { it.ratingAuthor }) { index, user ->
            UserCardChart(user, index + 1)
        }
    }
}

@Composable
fun UserCardChart(user: User,rank: Int) {
    Box(
        modifier = Modifier
            .background(color = Color.Black)
    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(bottom = 7.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(user.photoUrl),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .height(90.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .background(color = Color.Transparent),
                elevation = 120.dp,
                backgroundColor = Color.Transparent
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.Transparent)
                        .fillMaxSize()
                ) {


                    Row(modifier = Modifier.background(Color.Transparent)) {
                        Text(
                            text = "$rank",
                            style = TextStyle(
                                fontSize = 24.sp,
                                lineHeight = 21.sp,
                                fontFamily = FontFamily(Font(R.font.codenext_book)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 1.02.sp,
                                color = Color(0xFFFFFFFF)
                            ),
                            modifier = Modifier
                                .background(Color.Transparent)
                                .padding(end = 8.dp)  // Добавляем отступ справа
                        )
                        Text(
                            text = user.name!!,
                            style = TextStyle(
                                fontSize = 24.sp,
                                lineHeight = 21.sp,
                                fontFamily = FontFamily(Font(R.font.codenext_book)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 1.02.sp,
                                color = Color(0xFFFFFFFF)
                            ), modifier = Modifier.background(Color.Transparent)
                        )
                        Text(
                            text = "${user.ratingAuthor}",
                            style = TextStyle(
                                fontSize = 17.sp,
                                lineHeight = 17.sp,
                                fontFamily = FontFamily(Font(R.font.codenext_book)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 0.7000000000000001.sp,
                                color = Color(0xFFFFFFFF)
                            ), modifier = Modifier.background(Color.Transparent)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistChart(playlists: List<Playlist>, navController: NavController, viewModel: PlaylistChartViewModel) {
    LazyColumn(
        Modifier
            .padding(bottom = 85.dp)
            .background(Color.Black)) {
        items(playlists, key = { it.id }) { playlist ->
            PlaylistCard(playlist, playlists.indexOf(playlist), navController = navController, viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlaylistCard(
    playlist: Playlist,
    raitcount: Int,
    navController: NavController,
    viewModel: PlaylistChartViewModel
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val threshold = 100
    if (raitcount < 3) {
        Box(
            modifier = Modifier


                .background(color = Color.Transparent)
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
                                    viewModel.updatePlaylistRating(playlist.id, 50)
                                } else if (offsetX.value < -threshold) {
                                    viewModel.updatePlaylistRating(playlist.id, -50)
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
            Box(modifier = Modifier
                .background(Color.Transparent)
                .padding(bottom = 7.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(playlist.photoUrl),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .height(90.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )

                Card(
                    modifier = Modifier
                        .fillMaxSize()

                        .align(Alignment.Center)
                        .combinedClickable(
                            onClick = {
                                val playlistId = playlist.id
                                viewModel.updatePlaylistRating(playlistId, 15)
                                navController.navigate("OpenPbPlayList/${playlistId}")
                            },
                            onLongClick = { scope.launch { sheetState.show() } },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple()
                        )
                        .background(color = Color.Transparent)
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) },
                    elevation = 120.dp,
                    backgroundColor = Color.Transparent
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = "№${raitcount + 1}",
                            style = TextStyle(
                                fontSize = 20.sp,
                                lineHeight = 25.sp,
                                fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 1.2.sp,
                                textAlign = TextAlign.Center,
                                color = Color(0xFFFFFFFF)
                            ),
                            modifier = Modifier
                                .padding(end = 20.dp, start = 9.dp)
                                .background(Color.Transparent)
                        )

                        Image(
                            painter = rememberAsyncImagePainter(playlist.photoUrl),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 37.dp)
                                .width(80.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .height(80.dp), contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.background(Color.Transparent)) {
                            Text(
                                text = playlist.name!!,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    lineHeight = 21.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_book)),
                                    fontWeight = FontWeight(700),
                                    letterSpacing = 1.02.sp,
                                    color = Color(0xFFFFFFFF)
                                ), modifier = Modifier.background(Color.Transparent)
                            )
                            Text(
                                text = "Очков: ${playlist.rating}",
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    lineHeight = 17.sp,
                                    fontFamily = FontFamily(Font(R.font.codenext_book)),
                                    fontWeight = FontWeight(700),
                                    letterSpacing = 0.7000000000000001.sp,
                                    color = Color(0xFFFFFFFF)
                                ), modifier = Modifier.background(Color.Transparent)
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = offsetX.value > 0,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .background(Color.Transparent)
                ) {
                    androidx.compose.material3.Text(
                        text = "+50",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 0.96.sp,
                            color = Color(0xFF4CAF50)
                        )
                    )
                }
                AnimatedVisibility(
                    visible = offsetX.value < 0,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(Color.Transparent)
                ) {
                    androidx.compose.material3.Text(
                        text = "-50",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 0.96.sp,
                            color = Color(0xFFF70404)
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
        }

    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .background(color = Color(0xCF000000))
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
                                    viewModel.updatePlaylistRating(playlist.id, 50)
                                } else if (offsetX.value < -threshold) {
                                    viewModel.updatePlaylistRating(playlist.id, -50)
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
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 15.dp)
                    .combinedClickable(
                        onClick = {
                            val playlistId = playlist.id
                            viewModel.updatePlaylistRating(playlistId, 15)
                            navController.navigate("OpenPbPlayList/${playlistId}")
                        },
                        onLongClick = { scope.launch { sheetState.show() } },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple()
                    )
                    .background(color = Color(0xCF000000))
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) },
                elevation = 10.dp,
                backgroundColor = Color(0xCF000000)
            ) {
                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "№${raitcount + 1}",
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 25.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 1.2.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFFFFFFF)
                        ),
                        modifier = Modifier.padding(end = 20.dp, start = 9.dp)
                    )
                    Image(
                        painter = rememberAsyncImagePainter(playlist.photoUrl),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 37.dp)
                            .width(80.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .height(80.dp), contentScale = ContentScale.Crop
                    )

                    Column {
                        Text(
                            text = playlist.name!!,
                            style = TextStyle(
                                fontSize = 17.sp,
                                lineHeight = 21.sp,
                                fontFamily = FontFamily(Font(R.font.codenext_book)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 1.02.sp,
                                color = Color(0xFFFFFFFF)
                            )
                        )
                        Text(
                            text = "Очков: ${playlist.rating}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 17.sp,
                                fontFamily = FontFamily(Font(R.font.codenext_book)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 0.7000000000000001.sp,
                                color = Color(0xFF8A9A9D)
                            )
                        )
                    }
                }
            }

            AnimatedVisibility(visible = offsetX.value > 0, modifier = Modifier.align(Alignment.CenterStart)) {
                androidx.compose.material3.Text(
                    text = "+50",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                        fontWeight = FontWeight(700),
                        letterSpacing = 0.96.sp,
                        color = Color(0xFF4CAF50)
                    )
                )
            }
            AnimatedVisibility(visible = offsetX.value < 0, modifier = Modifier.align(Alignment.CenterEnd)) {
                androidx.compose.material3.Text(
                    text = "-50",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                        fontWeight = FontWeight(700),
                        letterSpacing = 0.96.sp,
                        color = Color(0xFFF70404)
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
    }
}






