package com.reyaz.connectcare

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.reyaz.connectcare.ble.BleScannerScreen
import com.reyaz.connectcare.ui.theme.ConnectCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        ActivityCompat.requestPermissions(this, permissions, 100)
        setContent {
            ConnectCareTheme {
                BleScannerScreen()
//                MainNavHost()
            }
        }
    }
}
