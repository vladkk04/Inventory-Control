package com.example.inventorycotrol.ui.fragments.organisationSettings

import com.example.inventorycotrol.data.remote.dto.NotificationSettings
import com.example.inventorycotrol.data.remote.dto.ThresholdSettings


data class OrganisationSettingsUiState(
    val notificationSettings: NotificationSettings = NotificationSettings(
        "7:00",
        emptySet(),
        emptySet()
    ),
    val thresholdSettings: ThresholdSettings = ThresholdSettings(
        0.00,
        0.00,
        0.00
    )
)