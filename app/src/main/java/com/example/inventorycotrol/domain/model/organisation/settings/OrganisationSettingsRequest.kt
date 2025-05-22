package com.example.inventorycotrol.domain.model.organisation.settings
import com.example.inventorycotrol.data.remote.dto.NotificationSettings
import com.example.inventorycotrol.data.remote.dto.ThresholdSettings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationSettingsRequest(
    @SerialName("threshold_settings")
    val thresholdSettings: ThresholdSettings,
    @SerialName("notification_settings")
    val notificationSettings: NotificationSettings
)