package com.example.bachelorwork.ui.model.organisationRole

import com.example.bachelorwork.domain.model.organisation.OrganisationRole


data class OrganisationRoleListUiState(
    val isLoading: Boolean = false,
    val organisationRoles: List<OrganisationRole> = emptyList(),
)