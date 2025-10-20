package com.reyaz.connectcare.ui.screens.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.reyaz.connectcare.repository.ble.BleManager
import com.reyaz.connectcare.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val bleManager: BleManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /**
     * Connect directly to a BLE device using its MAC address.
     * Emits heart rate updates to [uiState.heartRate].
     */
    fun connectToDevice(macAddress: String) {
        Log.d("HomeViewModel", "Connecting to device with MAC: $macAddress")
        _uiState.value = _uiState.value.copy(
            isConnecting = true,
            connectedDeviceMac = macAddress,
            heartRate = null,
            errorMessage = null
        )

        try {
            bleManager.connectByAddress(macAddress) { heartRate ->
                // Emit each heart rate update
                viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        heartRate = heartRate,
                        isConnecting = false
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isConnecting = false,
                errorMessage = e.message
            )
        }
    }

    /**
     * Disconnect from the currently connected BLE device.
     */
    fun disconnectDevice() {
        bleManager.disconnect()
        _uiState.value = _uiState.value.copy(
            isConnecting = false,
            heartRate = null,
            connectedDeviceMac = null
        )
    }
}