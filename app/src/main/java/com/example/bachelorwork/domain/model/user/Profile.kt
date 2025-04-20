package com.example.bachelorwork.domain.model.user

import com.example.bachelorwork.domain.model.organisation.OrganisationRole

data class Profile(
    val id: String,
    val fullName: String,
    val imageUrl: String?,
    val organisationRole: OrganisationRole,
)
