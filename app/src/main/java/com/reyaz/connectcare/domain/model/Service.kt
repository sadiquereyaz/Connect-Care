package com.reyaz.connectcare.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.reyaz.connectcare.R
import java.util.UUID

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