package com.gammamusic.ui.navigation.MainNavigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
                    contentDescription = "Icon", modifier = Modifier.width(25.dp).height(25.dp))},
                label = {Text(text = item.title,
                    style = TextStyle(
                        fontSize = 11.sp,
                        lineHeight = 13.sp,

                        fontWeight = FontWeight(700),
                        letterSpacing = 0.55.sp,
                        color = Color(0xFFFFFFFF)
                    )
                )},
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF00C2CB),
                    indicatorColor = Color.Black,
                    unselectedIconColor = Color.White
                ),
            )

        }
    }


}