package com.gammamusic.ui.navigation.MainNavigation

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(){
    val navController  = rememberNavController()
    Scaffold(

        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {

            NavGraph(navHostController = navController)

    }
}