package com.example.inventorycotrol.ui.model.organisationRole

import com.example.inventorycotrol.domain.model.organisation.OrganisationRole


data class OrganisationRoleListUiState(
    val isLoading: Boolean = false,
    val organisationRoles: List<OrganisationRole> = emptyList(),
)