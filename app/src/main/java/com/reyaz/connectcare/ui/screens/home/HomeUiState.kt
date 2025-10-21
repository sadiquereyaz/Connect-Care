package com.reyaz.connectcare.ui.screens.home

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.reyaz.connectcare.R
import kotlinx.parcelize.Parcelize
import java.util.UUID

data class HomeUiState(
    val isConnecting: Boolean = false,
    val heartRate: Int? = null,
    val spo2: Int? = null,
    val bodyTemperature: Float? = null,
    val bloodPressure: Pair<Int, Int>? = null, // systolic/diastolic
    val errorMessage: String? = null,
    val connectedDevice: IotDevice? = null
)

@Parcelize
data class IotDevice(
    val name: String,
    val macAddress: String
) : Parcelable

enum class Service(
    val displayName: String,
    val serviceUuid: UUID,
    val characteristicUuid: UUID,
    val descriptorUuid: UUID?,
    val unit: String,
    @DrawableRes val icon: Int,
    val color: Color
) {

    HEART_RATE(
        displayName = "Heart\nRate",
        serviceUuid = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb"),
        characteristicUuid = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb"),
        descriptorUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"),
        unit = "BPM",
        icon = R.drawable.ic_heart, // replace with your drawable
        color = Color(0xFFE53935) // red
    ),

    SPO2(
        displayName = "SpO₂\nLevel",
        serviceUuid = UUID.fromString("00001822-0000-1000-8000-00805f9b34fb"),
        characteristicUuid = UUID.fromString("00002A5F-0000-1000-8000-00805f9b34fb"),
        descriptorUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"),
        unit = "%",
        icon = R.drawable.ic_spo2, // replace with your drawable
        color = Color(0xFF1E88E5) // blue
    ),

    THERMOMETER(
        displayName = "Body\nTemperature",
        serviceUuid = UUID.fromString("00001809-0000-1000-8000-00805f9b34fb"),
        characteristicUuid = UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"),
        descriptorUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"),
        unit = "°C",
        icon = R.drawable.ic_thermometer, // replace with your drawable
        color = Color(0xFFFFA000) // amber
    ),

    BLOOD_PRESSURE(
        displayName = "Blood\nPressure",
        serviceUuid = UUID.fromString("00001810-0000-1000-8000-00805f9b34fb"),
        characteristicUuid = UUID.fromString("00002A35-0000-1000-8000-00805f9b34fb"),
        descriptorUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"),
        unit = "mmHg",
        icon = R.drawable.ic_blood_pressure, // replace with your drawable
        color = Color(0xFF8E24AA) // purple
    );
}
