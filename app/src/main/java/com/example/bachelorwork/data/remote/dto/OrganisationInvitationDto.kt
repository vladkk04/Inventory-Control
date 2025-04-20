package com.example.bachelorwork.data.remote.dto

import com.example.bachelorwork.domain.model.organisation.OrganisationRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationInvitationDto(
    @SerialName("id")
    val id: String,
    @SerialName("organisation_name")
    val organisationName: String,
    @SerialName("organisation_role")
    val organisationRole: OrganisationRole,
    val status: StatusInvitation,
    @SerialName("invited_by")
    val invitedBy: String,
    @SerialName("expire_at")
    val expireAt: Long,
)

enum class StatusInvitation {
    PENDING,
    ACCEPTED,
    REJECTED
}
