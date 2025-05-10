package com.example.inventorycotrol.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationSettingsDto(
    val id: String,
    @SerialName("notification_settings")
    val notificationSettings: NotificationSettings,
    @SerialName("threshold_settings")
    val thresholdSettings: ThresholdSettings,
    @SerialName("organisation_id")
    val organisationId: String
)
