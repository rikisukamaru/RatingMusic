

package com.gammamusic.ui.screens.Autorization

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults


import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color

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
import com.gammamusic.R
import com.gammamusic.presentation.sign_in.SignInState




@Composable
fun Login(navController: NavController,state: SignInState,onSignInClick:()->Unit) {
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
        Column (
                Modifier
                        .fillMaxSize(1f)
                        .background(color = Color(0xFF1E1E1E)), horizontalAlignment = Alignment.CenterHorizontally){
                Image(painterResource(id = R.drawable.musium_logo),  contentDescription ="" ,
                        Modifier
                                .padding(top = 76.dp, bottom = 30.dp)
                                .width(325.dp)
                                .height(254.dp)

                )
                Text(
                        text = "Let’s get you in",
                        style = TextStyle(
                                fontSize = 44.sp,
                                lineHeight = 1.14.sp,
                                fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 1.3199999999999998.sp,
                                textAlign = TextAlign.Center,
                                color = Color(0xFFFFFFFF)
                        ),
                        modifier = Modifier.padding(bottom = 50.dp)
                )
                Button(
                        onClick = {
                                onSignInClick()
                        },
                        Modifier
                                .padding(top = 5.dp, bottom = 20.dp)
                                .width(343.dp)
                                .height(59.dp)
                                .background(color = Color(0xFF1E1E1E))
                                .border(
                                        width = 0.3.dp,
                                        color = Color(0xFFDBE7E8),
                                        shape = RoundedCornerShape(10.dp)
                                ),
                        shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(Color(0xFF1E1E1E))
                ) {
                        Image(painter = painterResource(id = R.drawable.google_search_new_logo_icon_159150), contentDescription ="",
                                Modifier
                                        .width(33.dp)
                                        .height(33.dp)
                                        .padding(end = 5.dp))
                        Text(text = "Продолжить с Google", style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 0.8.sp,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                lineHeight = 1.1.sp,
                        ))
                }
                Row(modifier = Modifier
                        .width(353.dp)
                        .height(25.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically)
                {
                        Text(text = "___________________", color = Color(0xFFFFFFFF), modifier = Modifier.padding(end = 5.dp))
                        Text(
                                text = "or",
                                style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 1.1.sp,
                                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                                        fontWeight = FontWeight(700),
                                        letterSpacing = 0.8.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(0xFFFFFFFF)
                                )
                        )
                        Text(text = "___________________", color = Color(0xFFFFFFFF), modifier = Modifier.padding(start = 5.dp))
                }
                Button(
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
                        Text(text = "Прододжить через логин", style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                                fontWeight = FontWeight(700),
                                letterSpacing = 0.8.sp,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                lineHeight = 1.1.sp,
                        ))
                }
                Row(modifier = Modifier.padding(10.dp)) {
                        Text(
                                text = "Отсутсвует аккаунт?",
                                style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 1.1.sp,
                                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
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
                                        fontFamily = FontFamily(Font(R.font.raleway_extralight)),
                                        fontWeight = FontWeight(700),
                                        letterSpacing = 0.8.sp,
                                        textAlign = TextAlign.Center,
                                        color = Color(0xFF39C0D4)
                                ),
                                modifier= Modifier
                                        .clickable { navController.navigate("Registered") }
                                        .shadow(elevation = 20.dp, spotColor = Color(0xFF06A0B5), ambientColor = Color(0xFF06A0B5), shape = RoundedCornerShape(10.dp) )
                        )
                }

        }


}



