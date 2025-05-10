package com.example.inventorycotrol.domain.model.organisation.user

import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationUserAssignRoleRequest(
    val role: OrganisationRole
)
