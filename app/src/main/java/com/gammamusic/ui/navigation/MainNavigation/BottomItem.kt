package com.gammamusic.ui.navigation.MainNavigation

import com.gammamusic.R


sealed class BottomItem(val title:String, val iconId: Int, val route:String){
    object MyMusic: BottomItem("Моя музыка", R.drawable.folder_ui,"MyMusic")
    object Rating: BottomItem("Рейтинг", R.drawable.rating,"Rating")
    object MyNewMusic: BottomItem("Главная", R.drawable.home,"MyNewMusic")
}
