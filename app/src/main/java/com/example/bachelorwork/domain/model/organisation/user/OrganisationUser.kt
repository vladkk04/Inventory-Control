package com.example.bachelorwork.domain.model.organisation.user

import com.example.bachelorwork.domain.model.organisation.OrganisationRole

data class OrganisationUser(
    val id: String,
    val organisationUserName: String,
    val organisationRole: OrganisationRole,
    val organisationUserStatus: OrganisationUserStatus,
    val userId: String?,
    val email: String?
)
