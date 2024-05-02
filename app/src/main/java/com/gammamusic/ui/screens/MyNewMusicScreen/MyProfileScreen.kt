package com.example.freelis.ui.screens.MyNewMusicScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults



import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter


import com.gammamusic.R
import com.gammamusic.domain.model.Playlist
import com.gammamusic.domain.model.Rating.User
import com.gammamusic.ui.screens.MyNewMusicScreen.MyProfileScreenViewModel
import com.gammamusic.ui.screens.RatingScreen.PlaylistChartViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyProfileScreen(viewModel: MyProfileScreenViewModel,navController: NavController) {
    val user by viewModel.user.observeAsState()
    val playlists by viewModel.playlists.observeAsState(emptyList())
    val vviewModel = PlaylistChartViewModel()
    val us:User? = user

    if (user != null) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)){
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(color = Color.Black)){
                Image(painter = rememberAsyncImagePainter(model = us!!.photoUrl) ?:painterResource(id = R.drawable.musium_logo), contentDescription ="", contentScale = ContentScale.Crop, modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight())
                Column(modifier = Modifier.align(Alignment.BottomStart)) {
                    Text(
                        text = us.name,
                        style = TextStyle(
                            fontSize = 36.sp,
                            lineHeight = 44.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(500),
                            textAlign = TextAlign.Center,
                            color = Color(0xFFFFFFFF)
                        ),
                        modifier = Modifier.padding(start = 30.dp, bottom = 10.dp)
                    )
                    Text(
                        text = if (us.ratingAuthor == 0) "На калибровке" else us.ratingAuthor.toString(),
                        style = TextStyle(
                            fontSize = 36.sp,
                            lineHeight = 44.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                            color = Color(0xFFFFFFFF)
                        ),
                        modifier = Modifier.padding(start = 30.dp, bottom = 10.dp)
                    )
                    Button(
                        onClick = {
                        },
                        Modifier
                            .padding(start = 28.dp, top = 5.dp, bottom = 100.dp)
                            .shadow(
                                elevation = 20.dp,
                                spotColor = Color(0xFF39C0D4),
                                ambientColor = Color(0xFF39C0D4)
                            )
                            .background(
                                color = Color(0xFF06A0B5),
                                shape = RoundedCornerShape(size = 50.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(Color(0xFF06A0B5))
                    ) {
                        Text(text = "Подписаться", style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(700),
                            letterSpacing = 0.8.sp,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            lineHeight = 1.1.sp,
                        ))
                    }
                }




            }
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.6f)){
                Image(painter = painterResource(id = R.drawable.shadowelipse), contentDescription ="", contentScale = ContentScale.FillBounds, modifier =
                Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f))
                Column {
                    Text(
                        text = "Лучшие плейлисты",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 19.sp,
                            fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                            fontWeight = FontWeight(1000),
                            textAlign = TextAlign.Center,
                            color = Color(0xFF00BCD4)
                        ),
                        modifier = Modifier.padding(start = 30.dp,top = 135.dp)
                    )
                    LazyRow (Modifier.fillMaxSize()){
                        items(playlists) { playlist ->
                            MyProfileCard(playlist,playlists.indexOf(playlist), navController = navController ,vviewModel)
                        }
                    }

                }


            }

        }
    } else {
        Text("Loading...", textAlign = TextAlign.Center)
    }



}


@Composable
fun MyProfileCard(playlist: Playlist, raitcount:Int, navController: NavController, viewModel: PlaylistChartViewModel) {
    Box(modifier = Modifier
        .background(Color.Black)
        .padding(start = 30.dp, top = 20.dp)
        )
    Column (
        Modifier
            .padding(top = 20.dp)
            .clickable {
                val playlistId = playlist.id
                viewModel.updatePlaylistRating(playlistId, 15)
                navController.navigate("OpenPbPlayList/${playlistId.toString()}")
            } ){
        Image(painter = rememberAsyncImagePainter(model = playlist.photoUrl), contentDescription = "", contentScale = ContentScale.FillHeight,modifier =
        Modifier
            .width(119.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(15.dp)))
        Text(
            text = playlist.name!!,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 15.sp,
                fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                fontWeight = FontWeight(1000),
                textAlign = TextAlign.Center,
                        color = Color(0xFFCBC8C8)
            )
        )
        Text(
            text = "Рейтинг:#${1+raitcount}",
            style = TextStyle(
                fontSize = 10.sp,
                lineHeight = 12.sp,
                fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
                color = Color(0xFF847D7D)
            )
        )
    }

}

