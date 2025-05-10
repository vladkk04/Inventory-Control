package com.example.inventorycotrol.ui.model.organisationUser

import com.example.inventorycotrol.domain.model.organisation.OrganisationRole

data class OrganisationUserManageUiState(
    val organisationRoles: List<OrganisationRole> = OrganisationRole.entries
)
