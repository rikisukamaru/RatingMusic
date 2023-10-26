package com.gammamusic.ui.screens.MusicPlayer

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.gammamusic.domain.model.Player.Track
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MusicPlayerScreen(id:Long) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val trackViewModel: TrackViewModel = viewModel()
    val trackState: Track? by trackViewModel.trackLiveData.observeAsState()
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        trackViewModel.getTrack(id)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Свернутый плеер
        Card(onClick = {scope.launch {
            sheetState.show()
        }},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 80.dp)
                .height(80.dp)
                .background(Color.White),
            elevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween


            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = trackState?.album?.cover_medium ?:"https://shutniks.com/wp-content/uploads/2020/01/unnamed-2.jpg"),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,            // crop the image if it's not a square
                    modifier = Modifier
                        .size(80.dp)
                        .wrapContentSize()   // add a border (optional)
                )
                Column {
                    Text(trackState?.title.toString(), Modifier.weight(1f))
                    Text(trackState?.artist?.name.toString(), Modifier.weight(1f))
                }
                Row {

                    IconButton(onClick = {
                        isPlaying = !isPlaying
                        if (isPlaying == true)
                        {
                            trackViewModel.play()
                        }else{
                            trackViewModel.stop()
                        }
                    }
                    ) {
                        val icon = if (isPlaying) Icons.Filled.Close else Icons.Filled.PlayArrow
                        Icon(icon, contentDescription = if (isPlaying) "Пауза" else "Воспроизвести")
                    }

                }

            }
        }

        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // Задайте желаемую высоту листа
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ){
                    FullScreenPlayer(trackState = trackState)
                }
            },
            sheetShape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
        ) {
        }
    }
}

@Composable
fun FullScreenPlayer(trackState: Track?) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = trackState?.album?.cover_medium ?:"https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgglSSzVyCKJ-nrkF-wjTNIWEV-LHCdSR7tfAbNsSKPvw8zzzUije8HstJpd6QiJZS2mq0sEXuQewaJ0KRdHSwZx7ogRIRfcbTSZzqpFmVP46VVeRk4nHz-RjfAzMq4b3Qiq3FL1uxlcLVYv9JCmkEPAxmu6OlttdbdUiD0Mrp-L9Rd5Addiu3fmVf-/s1280/bottom%20sheet.jpg"),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)                       // clip to the circle shape
                .wrapContentSize()   // add a border (optional)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(bottom = 100.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var progress by remember { mutableStateOf(0.8f) }
        ManipulatableSemiCircularProgressBar(
            progress = progress,
            onProgressChanged = { newProgress ->
                progress = newProgress
            },
        )
    }
}

@Composable
fun ManipulatableSemiCircularProgressBar(
    progress: Float,
    onProgressChanged: (Float) -> Unit,
) {
    var targetProgress by remember { mutableStateOf(progress) }
    val animatedProgress by animateFloatAsState(targetProgress)

    Box(
        modifier = Modifier
            .size(300.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        val normalizedDrag = dragAmount.x / size.width
                        val newProgress = targetProgress + normalizedDrag
                        targetProgress = newProgress.coerceIn(0f, 1f)
                        onProgressChanged(targetProgress)

                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            val startAngle = -240f
            val sweep = animatedProgress * 300f

            // Background semicircle
            drawArc(
                color = Color.White,
                startAngle = startAngle,
                sweepAngle = 300f,
                useCenter = false,
                style = Stroke(2.dp.toPx(), cap = StrokeCap.Butt)
            )

            // Progress semicircle
            drawArc(
                color = Color.Red,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(5.dp.toPx(), cap = StrokeCap.Butt)
            )
        }
    }
}

