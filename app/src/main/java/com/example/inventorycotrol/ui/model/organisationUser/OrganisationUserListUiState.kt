package com.example.inventorycotrol.ui.model.organisationUser

import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUser

data class OrganisationUserListUiState(
    val organisationUsers: List<OrganisationUser> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val ownId: String? = null
)
