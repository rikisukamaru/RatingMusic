package com.gammamusic.ui.navigation.MainNavigation

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gammamusic.presentation.sign_in.GoogleAuthUiClient


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController_login: NavController, googleAuthUiClient: GoogleAuthUiClient){
    val navController  = rememberNavController()
    Scaffold(

        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {

            NavGraph(nav_login = navController_login,navHostController = navController, googleAuthUiClient = googleAuthUiClient)

    }
}