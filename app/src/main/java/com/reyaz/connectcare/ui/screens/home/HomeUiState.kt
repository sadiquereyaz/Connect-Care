package com.reyaz.connectcare.ui.screens.home

import android.bluetooth.BluetoothDevice

data class HomeUiState(
    val isConnecting: Boolean = false,
    val heartRate: Int? = null,
    val errorMessage: String? = null,
    val connectedDeviceMac: String? = null
)

