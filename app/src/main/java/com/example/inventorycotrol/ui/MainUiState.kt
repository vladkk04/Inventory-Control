package com.example.inventorycotrol.ui

import com.example.inventorycotrol.domain.model.organisation.OrganisationItem
import com.example.inventorycotrol.domain.model.user.Profile

data class MainUiState(
    val organisations: List<OrganisationItem> = emptyList(),
    val selectedOrganisationId: String? = null,
    val profile: Profile? = null,
)
