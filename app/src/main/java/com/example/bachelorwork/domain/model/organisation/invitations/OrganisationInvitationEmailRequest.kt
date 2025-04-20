package com.example.bachelorwork.domain.model.organisation.invitations

import com.example.bachelorwork.domain.model.organisation.OrganisationRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OrganisationInvitationEmailRequest(
    @SerialName("organisation_user_name")
    val organisationUserName: String,
    @SerialName("organisation_role")
    val organisationRole: OrganisationRole,
    val email: String,
)
