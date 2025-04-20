package com.example.bachelorwork.ui.model.organisationUser

import com.example.bachelorwork.domain.model.organisation.user.OrganisationUser

data class OrganisationUserListUiState(
    val organisationUsers: List<OrganisationUser> = emptyList(),
    val ownId: String? = null
)
