

package com.gammamusic.ui.screens.Autorization

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gammamusic.presentation.sign_in.SignInState
import com.gammamusic.ui.navigation.MainNavigation.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
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

        var email by remember {
                mutableStateOf("")
        }
        var password by remember {
                mutableStateOf("")
        }

        Column(
                Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        ) {
                Row (  modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Text(text = "LOGIN",  modifier = Modifier
                                .padding(top = 300.dp), color = Color.White)
                }

                TextField(
                        value = email,
                        onValueChange = {email = it} ,
                        placeholder = { Text(text = "Email ID")},
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, bottom = 35.dp, top = 35.dp)
                        )
                TextField(value = password,
                        onValueChange = {password = it} ,
                        placeholder = { Text(text = "Password")},
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp))
                Row (horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()){
                        Button(onClick = {
                               navController.navigate("MainScreen")
                        }, Modifier
                                .padding(top = 40.dp)
                                .fillMaxWidth(0.5f)
                        ) {
                                Text(text = "SIGN IN")
                        }

                }
                Row (horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()) {
                        Button(
                                onClick = {  }, Modifier
                                        .padding(top = 40.dp)
                                        .fillMaxWidth(0.5f)
                        ) {
                                Text(text = "REGISTERED")
                        }
                }
                Row (horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()) {
                        Button(
                                onClick = {
                                        onSignInClick()
                                }, Modifier
                                        .padding(top = 40.dp)
                                        .fillMaxWidth(0.5f)
                        ) {
                                Text(text = "GOOGLE Auth")
                        }
                }
        }

}

