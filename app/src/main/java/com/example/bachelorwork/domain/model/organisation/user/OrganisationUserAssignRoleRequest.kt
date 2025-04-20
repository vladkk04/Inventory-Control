package com.example.bachelorwork.domain.model.organisation.user

import com.example.bachelorwork.domain.model.organisation.OrganisationRole
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationUserAssignRoleRequest(
    val role: OrganisationRole
)
