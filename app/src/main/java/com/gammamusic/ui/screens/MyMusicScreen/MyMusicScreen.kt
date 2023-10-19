package com.gammamusic.ui.screens.MyMusicScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.freelis.ui.screens.MyMusicScreen.MyMusicViewModel
import com.gammamusic.ui.navigation.MainNavigation.ScreensInMyMusic



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMusicScreen(
    navController: NavController,
    viewModel: MyMusicViewModel = MyMusicViewModel()
) {
    var text by remember { mutableStateOf("итм") }
    
    val trackResult by viewModel.trackResult.observeAsState()
    
    Column (modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
            ){
        //Апи на поиск по артисту
        Row {
            TextField(value = text, onValueChange = {text = it},
                Modifier
                    .fillMaxWidth(0.8f)
                    .background(Color.White))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {  viewModel.getTrackResult() },
            ) {
                Text(text = "выполнить")

            }

            trackResult?.let {
                Text(text = it.first().title, color = Color.White)
            }

        }



                Button(
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier.padding(5.dp),
                    onClick = {
                        navController.navigate(ScreensInMyMusic.MyMusicCollection.route)
                    }
                ) {
                    Text(
                        text = "Моя музыкальная коллекция",
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        color = Color.Black
                        )

                }
                Button(
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier.padding(5.dp),
                    onClick = {
                        navController.navigate(ScreensInMyMusic.MyPlaylistCollection.route)
                    }
                ) {
                    Text(
                        text = "Мои плейлисты",
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        color = Color.Black
                    )

                }

    }
}

