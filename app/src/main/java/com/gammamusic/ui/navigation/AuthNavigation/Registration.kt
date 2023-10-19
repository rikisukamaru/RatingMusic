package com.gammamusic.ui.navigation.AuthNavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Registration() {
    Column (modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ){ Text(text = "Ищи новую музыку") }

}