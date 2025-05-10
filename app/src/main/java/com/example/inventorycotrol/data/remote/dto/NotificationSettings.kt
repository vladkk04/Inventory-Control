package com.example.inventorycotrol.data.remote.dto

import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettings(
    @SerialName("notification_time")
    val notificationTime: String,
    @SerialName("notification_days")
    val notificationDays: Set<Int>,
    @SerialName("notifiable_roles")
    val notifiableRoles: Set<OrganisationRole>
)