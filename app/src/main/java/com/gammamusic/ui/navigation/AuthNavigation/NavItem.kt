package com.gammamusic.ui.navigation.AuthNavigation

sealed class NavItem(val route:String) {
    object login:NavItem("LOGIN")
    object registered:NavItem("Registered")
}