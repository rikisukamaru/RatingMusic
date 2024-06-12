package com.gammamusic


import android.os.Bundle

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import com.gammamusic.presentation.sign_in.GoogleAuthUiClient
import com.gammamusic.presentation.sign_in.SignInViewModel
import com.gammamusic.ui.navigation.AuthNavigation.Registration
import com.gammamusic.ui.navigation.MainNavigation.MainScreen
import com.gammamusic.ui.screens.Autorization.Login

import com.gammamusic.ui.screens.Splash_Screen.SplashScreen
import com.gammamusic.ui.theme.GammaMusicTheme

import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = com.google.android.gms.auth.api.identity.Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GammaMusicTheme {
                val navController = rememberNavController()

                // Проверка авторизации при запуске приложения
                if (googleAuthUiClient.isUserAuthenticated()) {
                    navController.navigate("MainScreen")
                } else {
                    NavHost(navController = navController, startDestination = "splash_screen" ){
                        composable("splash_screen"){
                            SplashScreen(navcontroller = navController)
                        }
                        composable("LOGIN"){
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signWithInIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                })

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(applicationContext, "Sign In Successful", Toast.LENGTH_LONG).show()
                                    navController.navigate("MainScreen")
                                }
                            }

                            Login(
                                navController = navController,
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                },
                                isUserAuthenticated = { googleAuthUiClient.isUserAuthenticated() }
                            )
                        }
                        composable("Registered") {
                            Registration(navController)
                        }
                        composable("MainScreen") {
                            MainScreen(navController, googleAuthUiClient)
                        }
                    }
                }
            }
        }
    }
}