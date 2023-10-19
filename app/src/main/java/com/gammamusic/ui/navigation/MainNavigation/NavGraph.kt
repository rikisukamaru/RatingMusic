package com.gammamusic.ui.navigation.MainNavigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.gammamusic.ui.screens.MyMusicScreen.MyMusicScreen
import com.example.freelis.ui.screens.MyMusicScreen.MyPlaylistCollection.MyPlaylistCollection
import com.example.freelis.ui.screens.MyNewMusicScreen.MyNewMusicScreen
import com.example.freelis.ui.screens.RatingScreen.RatingScreen

import com.gammamusic.ui.screens.MusicPlayer.MusicPlayerScreen

import com.gammamusic.ui.screens.MyMusicScreen.MyMusicCollection.MyMusicCollection


@Composable
fun NavGraph(navHostController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()){

        NavHost(navController = navHostController, startDestination = "MyMusic" ){
            composable("MyMusic"){
                MyMusicScreen(navHostController)
            }
            composable("MyNewMusic"){
                MyNewMusicScreen()
            }
            composable("Rating"){
                RatingScreen()
            }
            composable(route = ScreensInMyMusic.MyMusicCollection.route){
                MyMusicCollection()
            }
            composable(route = ScreensInMyMusic.MyPlaylistCollection.route){
                MyPlaylistCollection()
            }



        }
        MusicPlayerScreen()
    }

}