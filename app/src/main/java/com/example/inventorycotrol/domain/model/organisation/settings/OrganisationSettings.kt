package com.example.inventorycotrol.domain.model.organisation.settings

import com.example.inventorycotrol.data.remote.dto.NotificationSettings
import com.example.inventorycotrol.data.remote.dto.ThresholdSettings

data class OrganisationSettings(
    val id: String,
    val notificationSettings: NotificationSettings,
    val thresholdSettings: ThresholdSettings,
    val organisationId: String
)