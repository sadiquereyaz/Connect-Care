package com.reyaz.connectcare.ui.screens.scan_dialog

import android.bluetooth.BluetoothDevice

data class ScanUiState(
    val isScanning: Boolean = true,
    val deviceList: List<BluetoothDevice> = emptyList(),
    val errorMessage: String? = null,
    val connectingDevice: BluetoothDevice? = null,
    val isConnecting: Boolean = false
)
