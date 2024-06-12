

package com.gammamusic.ui.screens.Autorization


import android.widget.Toast
import androidx.compose.foundation.Image

import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults


import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur

import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import coil.compose.rememberAsyncImagePainter

import com.gammamusic.R
import com.gammamusic.presentation.sign_in.SignInState

@Composable
fun Login(navController: NavController,state: SignInState,onSignInClick:()->Unit, isUserAuthenticated: () -> Boolean) {
        val context = LocalContext.current
        LaunchedEffect(key1 = state.signInError){
                state.signInError?.let {
                        error->
                        Toast.makeText(
                                context,
                                error,
                                Toast.LENGTH_LONG
                        ).show()
                }
        }
        Box(modifier = Modifier.fillMaxSize() ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter ) {
                        Image(
                                painter = rememberAsyncImagePainter(model = R.drawable.loginbh),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().blur(radius = 7.dp)
                        )
                        Image(
                                painter = painterResource(id = R.drawable.rmlogo),
                                contentDescription = "logo",
                                modifier = Modifier.size(width = 350.dp, height = 350.dp)
                        )
                }
                Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize())
                {
                        Image(
                                painter = rememberAsyncImagePainter(model = R.drawable.blackbg),
                                contentDescription = "",
                                Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.3f)
                                        .graphicsLayer(alpha = 0.85f),
                                contentScale = ContentScale.FillBounds
                        )
                        Column {
                                Button(
                                        onClick = {
                                                        onSignInClick()
                                        },
                                        Modifier
                                                .padding(
                                                        top = 70.dp,
                                                        start = 20.dp,
                                                        end = 20.dp
                                                )
                                                .fillMaxWidth()
                                                .fillMaxHeight(0.08f)
                                                .graphicsLayer(alpha = 0.9f),
                                        shape = RoundedCornerShape(50.dp),
                                        colors = ButtonDefaults.buttonColors(Color(0xFFFFFFFF))
                                ) {
                                        Image(
                                                painter = painterResource(id = R.drawable.google_search_new_logo_icon_159150),
                                                contentDescription = "",
                                                Modifier
                                                        .width(33.dp)
                                                        .height(33.dp)
                                                        .padding(end = 5.dp)
                                        )
                                        Text(
                                                text = "Продолжить с Google", style = TextStyle(
                                                        fontFamily = FontFamily(Font(R.font.codenext_book)),
                                                        fontWeight = FontWeight(700),
                                                        letterSpacing = 0.8.sp,
                                                        textAlign = TextAlign.Center,
                                                        fontSize = 16.sp,
                                                        lineHeight = 1.1.sp,
                                                        color = Color(0xFF000000)
                                                )
                                        )
                                }
                                Box(contentAlignment = Alignment.Center) {
                                        Box(contentAlignment = Alignment.Center,modifier = Modifier.padding(bottom = 35.dp)
                                                .clickable {
                                                        if (isUserAuthenticated()) {
                                                                navController.navigate("MainScreen")
                                                        } else {
                                                                Toast.makeText(context, "Пользователь не авторизован", Toast.LENGTH_LONG).show()
                                                        }}){
                                                Image(
                                                        painter = rememberAsyncImagePainter(model = R.drawable.redbutton),
                                                        contentDescription = "",
                                                        modifier = Modifier
                                                                .fillMaxWidth()
                                                                .fillMaxHeight(0.2f)

                                                )
                                                Text(
                                                        text = "Продолжить через логин", style = TextStyle(
                                                                fontFamily = FontFamily(Font(R.font.codenext_book)),
                                                                fontWeight = FontWeight(700),
                                                                letterSpacing = 0.8.sp,
                                                                textAlign = TextAlign.Center,
                                                                fontSize = 18.sp,
                                                                lineHeight = 1.1.sp,
                                                                color = Color(0xFFFFFFFF)
                                                        )
                                                )
                                        }

                                        Row(modifier = Modifier.padding(top = 80.dp)) {
                                                Text(
                                                        text = "Отсутсвует аккаунт?",
                                                        style = TextStyle(
                                                                fontSize = 16.sp,
                                                                lineHeight = 1.1.sp,
                                                                fontFamily = FontFamily(Font(R.font.codenext_book)),
                                                                fontWeight = FontWeight(700),
                                                                letterSpacing = 0.8.sp,
                                                                textAlign = TextAlign.Center,
                                                                color = Color(0xFFFFFFFF)
                                                        )
                                                )
                                                Text(
                                                        text = " Авторизоваться",
                                                        style = TextStyle(
                                                                fontSize = 16.sp,
                                                                lineHeight = 1.1.sp,
                                                                fontFamily = FontFamily(Font(R.font.codenext_extrabold)),
                                                                fontWeight = FontWeight(700),
                                                                letterSpacing = 0.8.sp,
                                                                textAlign = TextAlign.Center,
                                                                color = Color(0xFFE80B7C)
                                                        ),
                                                        modifier = Modifier
                                                                .clickable { navController.navigate("Registered") }
                                                                .shadow(
                                                                        elevation = 20.dp,
                                                                        spotColor = Color(0xFFE80B7C),
                                                                        ambientColor = Color(
                                                                                0xDAE80B7C
                                                                        ),
                                                                        shape = RoundedCornerShape(10.dp)
                                                                )
                                                )
                                        }
                                }
                        }

                }
        }

}




