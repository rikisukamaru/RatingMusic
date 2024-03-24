package com.gammamusic.ui.navigation.AuthNavigation


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gammamusic.R


@Composable
fun Registration(navController:NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

    ) {
        Image(painter = painterResource(id = R.drawable.musium_logo), contentDescription = "", modifier = Modifier
            .width(275.dp)
            .padding(top = 9.dp)
            .height(215.dp))
        Text(
            text = "Create your account",
            style = TextStyle(
                fontSize = 32.sp,
                lineHeight = 1.14.sp,
                fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                fontWeight = FontWeight(700),
                letterSpacing = 0.96.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFFFFFFFF)
            ), modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp)
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(
                text = "Email",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 1.1.sp,
                    fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                    fontWeight = FontWeight(400),
                    letterSpacing = 0.8.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFFFFFFF)
                )
            ) },
            modifier = Modifier
                .padding(top = 44.dp)
                .width(377.dp)
                .height(59.dp)
                .border(width = 0.2.dp, color = Color(0xFFDBE7E8), shape = RoundedCornerShape(size = 10.dp))
                .background(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(size = 10.dp))
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(
                text = "Password",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 1.1.sp,
                    fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                    fontWeight = FontWeight(400),
                    letterSpacing = 0.8.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFFFFFFF)
                )
            ) },
            modifier = Modifier
                .padding(top = 16.dp)
                .width(377.dp)
                .height(59.dp)
                .border(width = 0.2.dp, color = Color(0xFFDBE7E8), shape = RoundedCornerShape(size = 10.dp))
                .background(color = Color(0xFF1E1E1E), shape = RoundedCornerShape(size = 10.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Button(
            onClick = {
                navController.navigate("MainScreen")
            },
            Modifier
                .padding(25.dp)
                .width(377.dp)
                .height(59.dp)
                .shadow(
                    elevation = 20.dp,
                    spotColor = Color(0xFF39C0D4),
                    ambientColor = Color(0xFF39C0D4)
                )
                .background(
                    color = Color(0xFF06A0B5),
                    shape = RoundedCornerShape(size = 50.dp)
                ),
            colors = ButtonDefaults.buttonColors(Color(0xFF06A0B5))
        ) {
            Text(
                text = "Создать аккаунт", style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                    fontWeight = FontWeight(700),
                    letterSpacing = 0.8.sp,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    lineHeight = 1.1.sp,
                )
            )
        }
    }
}
