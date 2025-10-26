package com.reyaz.connectcare.ui.screens.home

import com.reyaz.connectcare.domain.model.IotDevice

data class HomeUiState(
    val isConnecting: Boolean = false,
    val heartRate: Int? = null,
    val spo2: Int? = null,
    val bodyTemperature: Float? = null,
    val bloodPressure: Pair<Int, Int>? = null, // systolic/diastolic
    val errorMessage: String? = null,
    val connectedDevice: IotDevice? = null
)