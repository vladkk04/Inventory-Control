package com.example.bachelorwork.domain.model.organisation.invitations

import com.example.bachelorwork.domain.model.organisation.OrganisationRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OrganisationInvitationUserIdRequest(
    @SerialName("organisation_user_name")
    val organisationUserName: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("organisation_role")
    val organisationRole: OrganisationRole,
)
