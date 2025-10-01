package com.reyaz.connectcare.ui.screens.video_call

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.SurfaceView
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.agora.rtc2.*
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VideoEncoderConfiguration

class AgoraViewModel(application: Application) : AndroidViewModel(application) {

    private val appId = "aa5295fa77ad4e19820e9198331c4e1a" // Replace with your Agora App ID

    private var rtcEngine: RtcEngine? = null

    // Reactive SurfaceViews
    var localView by mutableStateOf<SurfaceView?>(null)
    var remoteView by mutableStateOf<SurfaceView?>(null)

    // Remote user ID
    private var remoteUid: Int? = null

    fun initRtcEngine(context: Context) {
        if (rtcEngine != null) return // Already initialized

        Log.d("AgoraViewModel", "Initializing RtcEngine")
        rtcEngine = RtcEngine.create(context, appId, object : IRtcEngineEventHandler() {

            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                Log.d("AgoraViewModel", "Joined channel $channel with uid $uid")
            }

            override fun onUserJoined(uid: Int, elapsed: Int) {
                Log.d("AgoraViewModel", "User joined: $uid")
                remoteUid = uid
                remoteView = RtcEngine.CreateRendererView(context)
                rtcEngine?.setupRemoteVideo(VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid))
            }

            override fun onUserOffline(uid: Int, reason: Int) {
                Log.d("AgoraViewModel", "User offline: $uid, reason=$reason")
                if (remoteUid == uid) {
                    remoteUid = null
                    remoteView = null
                }
            }
        })

        // Configure video
        rtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
        rtcEngine?.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        rtcEngine?.enableVideo()
        rtcEngine?.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )

        setupLocalVideo(context)
    }

    private fun setupLocalVideo(context: Context) {
        Log.d("AgoraViewModel", "Setting up local video")
        localView = RtcEngine.CreateRendererView(context)
        rtcEngine?.setupLocalVideo(VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0))
        rtcEngine?.startPreview()
        Log.d("AgoraViewModel", "Local preview started")
    }

    fun joinChannel(token: String, channel: String, optionalUid: Int = 0) {
        if (rtcEngine == null) {
            Log.e("AgoraViewModel", "RtcEngine not initialized")
            return
        }
        Log.d("AgoraViewModel", "Joining channel: $channel with token: $token")
        rtcEngine?.joinChannel(token, channel, "", optionalUid)
    }

    fun leaveChannel() {
        Log.d("AgoraViewModel", "Leaving channel")
        rtcEngine?.leaveChannel()
        rtcEngine?.stopPreview()
        localView = null
        remoteView = null
        remoteUid = null
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AgoraViewModel", "Destroying RtcEngine")
        RtcEngine.destroy()
        rtcEngine = null
    }
}
