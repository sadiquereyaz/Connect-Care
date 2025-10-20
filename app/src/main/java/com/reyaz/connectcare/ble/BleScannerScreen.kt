package com.reyaz.connectcare.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
/*
@SuppressLint("MissingPermission")
@Composable
fun BleScannerScreen(
    modifier: Modifier = Modifier.systemBarsPadding()
) {
    val context = LocalContext.current
    val bleManager = remember { BleManager(context) }
    var devices by remember { mutableStateOf(setOf<BluetoothDevice>()) }
    var receivedData by remember { mutableStateOf("") }

    Column(modifier.padding(16.dp)) {

        Button(
            onClick = {
                bleManager.startScan {
                    devices = devices + it
                }
            }
        ) {
            Text("Scan Devices")
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(devices.toList()) { device ->
                device.name?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                bleManager.connectToDevice(device) { data ->
                                    receivedData = data
                                }
                            }
                            .padding(8.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Received Data: $receivedData")
    }
}*/
