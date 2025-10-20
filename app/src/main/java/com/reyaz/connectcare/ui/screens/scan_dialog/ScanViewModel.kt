package com.reyaz.connectcare.ui.screens.scan_dialog

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.connectcare.repository.ble.BleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")

class ScanViewModel(
    private val bleManager: BleManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState = _uiState.asStateFlow()

    init {
        startScanning()
    }

    fun startScanning() {
        viewModelScope.launch {
            bleManager.scanNearbyBleDevices(scanDuration = 10_000L) { scanResult, isScanning ->
                scanResult.onSuccess { devices ->
                    _uiState.update {
                        it.copy(
                            deviceList = devices,
                            isScanning = isScanning,
                            errorMessage = null
                        )
                    }
                }.onFailure {
                    _uiState.update {
                        it.copy(
                            isScanning = false,
                            errorMessage = it.errorMessage
                        )
                    }
                }
            }
        }
    }

    fun stopScanning() {
        viewModelScope.launch {
            bleManager.stopScan()
        }
    }
}