package com.reyaz.connectcare.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IotDevice(
    val name: String,
    val macAddress: String
) : Parcelable