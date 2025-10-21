package com.reyaz.connectcare.ui.screens.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.connectcare.repository.ble.BleManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val bleManager: BleManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun setConnectedDevice(device: IotDevice) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(connectedDevice = device)
            }
        }
    }

    fun observeHeartRate() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isConnecting = true,
                heartRate = null,
                errorMessage = null
            )

            try {
                uiState.value.connectedDevice?.macAddress?.let { macAddress ->
                    bleManager.connectByAddress(macAddress) { heartRate ->
                        // Emit each heart rate update
                        viewModelScope.launch {
                            _uiState.value = _uiState.value.copy(
                                heartRate = heartRate,
                                isConnecting = false
                            )
                        }
                    }
                } ?: throw Exception("MAC address is null")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error connecting to device", e)
                _uiState.value = _uiState.value.copy(
                    isConnecting = false,
                    errorMessage = e.message
                )
            }
        }
    }

    /**
     * Disconnect from the currently connected BLE device.
     */
    fun disconnectDevice() {
        bleManager.disconnect()
        _uiState.value = _uiState.value.copy(
            isConnecting = false,
            connectedDevice = null
        )
    }
}