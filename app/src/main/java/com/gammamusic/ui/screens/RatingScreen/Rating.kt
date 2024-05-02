package com.example.freelis.ui.screens.RatingScreen


import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

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
                .background(Color(0xCF000000))
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> UserRatingChart(userChatViewModel = usersViewModel)
                1 -> PlaylistChart(playlistViewModel.playlists.value, navController = navController,playlistViewModel)
            }
        }
    }
}
@Composable
fun UserRatingChart(userChatViewModel: UserChatViewModel) {
    val users by userChatViewModel.users.observeAsState(initial = emptyList())

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(users) { user ->
            UserCardChart(user)

        }
    }
}

@Composable
fun UserCardChart(user: User) {

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(Color.Black)
            .padding(start = 25.dp, end = 25.dp)) {
        Image(painter = rememberAsyncImagePainter(model = user!!.photoUrl)?:painterResource(id = R.drawable.sdf), contentDescription ="", contentScale = ContentScale.Crop ,modifier = Modifier
            .fillMaxSize()
            .clip(
                RoundedCornerShape(16.dp)
            ))
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = user.name,
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 25.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                        fontWeight = FontWeight(700),
                        letterSpacing = 1.2.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier.padding(end = 25.dp, start = 9.dp)
                )
            Text(
                text = user.ratingAuthor.toString(),
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 25.sp,
                    fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                    fontWeight = FontWeight(700),
                    letterSpacing = 1.2.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFFFFFFF)
                ),
                modifier = Modifier.padding(end = 25.dp, start = 9.dp)
            )


        }

    }
}

@Composable
fun PlaylistChart(playlists: List<Playlist>,navController: NavController,viewModel: PlaylistChartViewModel) {
    LazyColumn (Modifier.padding(bottom = 85.dp)){
        items(playlists) { playlist ->
            PlaylistCard(playlist,playlists.indexOf(playlist), navController = navController ,viewModel)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun PlaylistCard(playlist: Playlist, raitcount:Int,navController: NavController,viewModel: PlaylistChartViewModel) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val anchors = mapOf(0f to 0, 370f to 1, -370f to -1) // Задаем точки привязки для свайпа
    val coroutineScope = rememberCoroutineScope()
    // Используем Animatable для отслеживания смещения карточки
    val offsetX = remember { Animatable(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(79.dp)
            .background(color = Color(0xCF1E1E1E))
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                thresholds = { _, _ -> FractionalThreshold(0.5f) }, // Устанавливаем порог свайпа
                reverseDirection = true // Разрешаем свайп в обоих направлениях
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val (x, _) = dragAmount
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + x)
                        }// Обновляем смещение карточки
                    },
                    onDragEnd = {
                        // Запускаем анимацию возврата в первоначальное положение внутри корутины
                        coroutineScope.launch {
                            offsetX.animateTo(targetValue = 0f)
                        }
                        if (offsetX.value > 70f) {

                            // Свайп вправо, увеличиваем рейтинг
                            viewModel.updatePlaylistRating(playlist.id, 15)

                        } else if (offsetX.value < -70f) {
                            // Свайп влево, уменьшаем рейтинг
                            viewModel.updatePlaylistRating(playlist.id, -15)

                        }
                    }
                )
            }
    ) {
        // Ваш контент карточки
        Card(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .width(370.dp)
                .height(79.dp)
                .combinedClickable(
                    onClick = {
                        val playlistId = playlist.id
                        viewModel.updatePlaylistRating(playlistId, 15)
                        navController.navigate("OpenPbPlayList/${playlistId.toString()}")
                    },
                    onLongClick = { scope.launch { sheetState.show() } },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple()
                )
                .background(color = Color(0xCF1E1E1E))
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }, // Применяем смещение к карточке
            elevation = 10.dp,
            backgroundColor = Color(0xCF1E1E1E)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#${1+raitcount}",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 25.sp,
                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                        fontWeight = FontWeight(700),
                        letterSpacing = 1.2.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier.padding(end = 25.dp, start = 9.dp)
                )
                Image(painter = rememberAsyncImagePainter(playlist.photoUrl), contentDescription ="",
                    modifier = Modifier
                        .padding(end = 37.dp)
                        .height(52.dp)
                        .width(53.dp))

                Column {
                    Text(
                        text = "${playlist.name}",
                        style = TextStyle(
                            fontSize = 17.sp,
                            lineHeight = 21.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
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
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 0.7000000000000001.sp,
                            color = Color(0xFF8A9A9D)
                        )
                    )
                }
            }
        }

        // Отображение иконок в зависимости от направления свайпа
        AnimatedVisibility(visible = offsetX.value > 0, modifier = Modifier.align(Alignment.CenterStart)) {
            androidx.compose.material3.Text(
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
            androidx.compose.material3.Text(
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
}




