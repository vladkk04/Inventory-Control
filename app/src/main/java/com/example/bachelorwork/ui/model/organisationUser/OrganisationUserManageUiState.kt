package com.example.bachelorwork.ui.model.organisationUser

import com.example.bachelorwork.domain.model.organisation.OrganisationRole

data class OrganisationUserManageUiState(
    val organisationRoles: List<OrganisationRole> = OrganisationRole.entries
)
