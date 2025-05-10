package com.example.inventorycotrol.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThresholdSettings(
    @SerialName("normal_threshold_percentage")
    val normalThresholdPercentage: Double,
    @SerialName("medium_threshold_percentage")
    val mediumThresholdPercentage: Double,
    @SerialName("critical_threshold_percentage")
    val criticalThresholdPercentage: Double,
)
