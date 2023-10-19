package com.gammamusic.ui.navigation.MainNavigation

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(navController: NavController) {
    val listItems = listOf(
        BottomItem.MyNewMusic,
        BottomItem.Rating,
        BottomItem.MyMusic
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    NavigationBar(containerColor = Color.Black) {
        listItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute==item.route,
                onClick={ navController.navigate(item.route)},
                icon = { Icon(painter = painterResource(id = item.iconId),
                    contentDescription = "Icon")},
                label = {Text(text = item.title, fontSize = 11.sp, color = Color.White)},
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Red,
                    indicatorColor = Color.Black,
                    unselectedIconColor = Color.White
                ),
            )

        }
    }


}