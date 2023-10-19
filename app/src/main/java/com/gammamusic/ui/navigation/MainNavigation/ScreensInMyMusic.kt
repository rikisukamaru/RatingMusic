package com.gammamusic.ui.navigation.MainNavigation

sealed class ScreensInMyMusic(val route:String){
        object MyMusicCollection: ScreensInMyMusic("MyMusicCollection")
        object MyPlaylistCollection: ScreensInMyMusic("MyPlaylistCollection")

}
