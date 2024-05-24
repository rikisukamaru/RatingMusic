package com.gammamusic.ui.screens.Splash_Screen

import android.view.animation.OvershootInterpolator

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.size

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur

import androidx.compose.ui.draw.scale

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.gammamusic.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navcontroller:NavController) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    val alpha = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 2.5f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(5f).getInterpolation(it)
                }
            )
        )
        delay(500L)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = LinearEasing
            )
        )
        delay(1000L) // Задержка перед переходом на следующий экран
        navcontroller.navigate("LOGIN")
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.loginbh),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().blur(radius = 7.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.rmlogo),
            contentDescription = "logo",
            modifier = Modifier.scale(scale.value).size(width = 150.dp, height = 150.dp)
        )

    }
}