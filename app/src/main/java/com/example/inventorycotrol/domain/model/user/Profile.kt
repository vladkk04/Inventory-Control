package com.example.inventorycotrol.domain.model.user

import com.example.inventorycotrol.domain.model.organisation.OrganisationRole

data class Profile(
    val id: String,
    val fullName: String,
    val imageUrl: String?,
    val email: String,
    val organisationRole: OrganisationRole,
)
