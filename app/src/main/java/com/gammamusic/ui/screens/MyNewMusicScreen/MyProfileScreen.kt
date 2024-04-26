package com.example.freelis.ui.screens.MyNewMusicScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.freelis.ui.screens.RatingScreen.PlaylistCard
import com.gammamusic.R
import com.gammamusic.domain.model.Rating.User
import com.gammamusic.ui.screens.MyNewMusicScreen.MyProfileScreenViewModel
import com.gammamusic.ui.screens.RatingScreen.PlaylistChartViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(viewModel: MyProfileScreenViewModel,navController: NavController) {
    val user by viewModel.user.observeAsState()
    val playlists by viewModel.playlists.observeAsState(emptyList())
   val vviewModel: PlaylistChartViewModel = PlaylistChartViewModel()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                Modifier.background(color = MaterialTheme.colorScheme.primary)
            )
        },
        content = {
            Column {
                if (user != null) {
                    MyProfileCard(user = user!!)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "My Playlists", style = MaterialTheme.typography.titleLarge)
                    LazyColumn {
                        items(playlists) { playlist ->
                            PlaylistCard(playlist,playlists.indexOf(playlist), navController = navController ,vviewModel)
                        }
                    }
                } else {
                    Text("Loading...", textAlign = TextAlign.Center)
                }
            }
        }
    )
}
@Composable
fun MyProfileCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = user.photoUrl),
                contentDescription = "User Photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = user.name, style = MaterialTheme.typography.titleLarge)
        }
    }
}
