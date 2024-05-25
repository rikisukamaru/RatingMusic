package com.gammamusic.ui.navigation.MainNavigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.gammamusic.ui.screens.MyMusicScreen.MyMusicScreen
import com.example.freelis.ui.screens.MyNewMusicScreen.MyProfileScreen
import com.example.freelis.ui.screens.RatingScreen.RatingScreen
import com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.PlayList.PlayListScreen
import com.gammamusic.ui.screens.MyMusicScreen.MyPlaylistCollection.PlayList.PlayListViewModel
import com.gammamusic.ui.screens.MyNewMusicScreen.MyProfileScreenViewModel
import com.gammamusic.ui.screens.RatingScreen.PublishedPlayList.pbPlayList
import com.gammamusic.ui.screens.RatingScreen.PublishedPlayList.pbPlayListViewModel


@Composable
fun NavGraph(navHostController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()){

        val playListViewModel = PlayListViewModel()
        val pbPlayListViewModel = pbPlayListViewModel()
        val MyProfileScreenViewModel = MyProfileScreenViewModel()

        NavHost(navController = navHostController, startDestination = "MyMusic" ){
            composable("MyMusic"){
                MyMusicScreen(navHostController)
            }
            composable("MyNewMusic"){
                MyProfileScreen(MyProfileScreenViewModel,navHostController)
            }
            composable("Rating"){
                RatingScreen(navHostController)
            }

            composable("PlayList/{playlistId}") { backStackEntry ->
                val playlistId = backStackEntry.arguments?.getString("playlistId")
                // Создайте экземпляр PlayListViewModel и передайте playlistId
                PlayListScreen(viewModel = playListViewModel, selectedPlaylistId = playlistId.orEmpty())
            }
            composable(route = "OpenPbPlayList/{playlistId}"){backStackEntry ->
                val playlistId = backStackEntry.arguments?.getString("playlistId")?: return@composable
                pbPlayList(navController = navHostController,pbPlayListViewModel,playlistId = playlistId )
            }


        }

    }

}