package com.reyaz.connectcare.ui.screens.video_call

import android.content.Context
import android.util.Log
import android.view.SurfaceView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AgoraVideoScreen(viewModel: AgoraViewModel = viewModel()) {
    val context = LocalContext.current
    var channelName by remember { mutableStateOf("testChannel112121") }
    var token by remember { mutableStateOf("") }

    // Initialize Agora engine safely
    DisposableEffect(context) {
        viewModel.initRtcEngine(context)
        onDispose { viewModel.leaveChannel() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
    ) {

        // Local Video
        viewModel.localView?.let { local ->
            AndroidView(
                factory = { local },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                update = { view ->
                    Log.d("AgoraDebug", "LocalView size: ${view.width}x${view.height}")
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Remote Video
        viewModel.remoteView?.let { remote ->
            AndroidView(
                factory = { remote },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                update = { view ->
                    Log.d("AgoraDebug", "RemoteView size: ${view.width}x${view.height}")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                Log.d("AgoraDebug", "Join button clicked")
                viewModel.joinChannel(token, channelName)
            }) { Text("Join Channel") }

            Button(onClick = {
                Log.d("AgoraDebug", "Leave button clicked")
                viewModel.leaveChannel()
            }) { Text("Leave Channel") }
        }
    }
}
