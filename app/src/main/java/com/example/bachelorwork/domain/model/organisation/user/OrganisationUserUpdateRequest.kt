package com.example.bachelorwork.domain.model.organisation.user

import kotlinx.serialization.Serializable

@Serializable
data class OrganisationUserUpdateRequest(
    val name: String
)
