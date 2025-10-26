package com.reyaz.connectcare.ui.screens.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.connectcare.domain.model.IotDevice
import com.reyaz.connectcare.domain.model.Service
import com.reyaz.connectcare.repository.ble.BleManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val bleManager: BleManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun setConnectedDevice(device: IotDevice) {
        _uiState.update { it.copy(connectedDevice = device) }
    }

    fun onErrorDismiss() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onGetParameterClick(service: Service) {
        when (service) {
            Service.HEART_RATE -> observeParameter(Service.HEART_RATE) { hr ->
                _uiState.update { it.copy(heartRate = hr) }
            }

            Service.SPO2 -> observeParameter(Service.SPO2) { spo2 ->
                // TODO: add SPO2 field in HomeUiState
                Log.d("BLE_DATA", "SpO2: $spo2")
            }

            Service.THERMOMETER -> observeParameter(Service.THERMOMETER) { temp ->
                Log.d("BLE_DATA", "Temp: $temp")
                _uiState.update { it.copy(bodyTemperature = temp.toFloat()) }
            }

            Service.BLOOD_PRESSURE -> observeParameter(Service.BLOOD_PRESSURE) { bp ->
                // TODO: add BP field in HomeUiState
                Log.d("BLE_DATA", "BP: $bp")
            }
        }
    }

    private fun observeParameter(service: Service, onResult: (Int) -> Unit) {
        val mac = uiState.value.connectedDevice?.macAddress
        if (mac.isNullOrEmpty()) {
            _uiState.update { it.copy(errorMessage = "No device connected") }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isConnecting = true, errorMessage = null) }

            try {
                bleManager.connectByAddress(service, mac) { result ->
                    _uiState.update { it.copy(isConnecting = false) }
                    onResult(result)
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error connecting to device", e)
                _uiState.update {
                    it.copy(isConnecting = false, errorMessage = e.localizedMessage)
                }
            }
        }
    }

    fun disconnectDevice() {
        bleManager.disconnect()
        _uiState.update {
            it.copy(isConnecting = false, connectedDevice = null)
        }
    }
}
