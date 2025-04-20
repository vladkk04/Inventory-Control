package com.example.bachelorwork.data.remote.dto

import com.example.bachelorwork.domain.model.organisation.OrganisationRole
import com.example.bachelorwork.domain.model.organisation.user.OrganisationUserStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OrganisationUserDto(
    val id: String,
    @SerialName("organisation_user_name")
    val organisationUserName: String,
    @SerialName("organisation_role")
    val organisationRole: OrganisationRole,
    @SerialName("organisation_user_status")
    val organisationUserStatus: OrganisationUserStatus,
    @SerialName("user_id")
    val userId: String?,
    val email: String?
)