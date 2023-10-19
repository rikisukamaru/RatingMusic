package com.gammamusic.ui.navigation.MainNavigation

import com.gammamusic.R


sealed class BottomItem(val title:String, val iconId: Int, val route:String){
    object MyMusic: BottomItem("Моя музыка", R.drawable.icon_library,"MyMusic")
    object Rating: BottomItem("Рейтинг", R.drawable.baseline_align_vertical_top_24,"Rating")
    object MyNewMusic: BottomItem("Новая музыка", R.drawable.icon_new_music,"MyNewMusic")
}
