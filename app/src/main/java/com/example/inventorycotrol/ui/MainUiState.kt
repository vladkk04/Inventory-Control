package com.example.inventorycotrol.ui

import com.example.inventorycotrol.domain.model.user.Profile
import com.example.inventorycotrol.ui.model.organisation.OrganisationItem

data class MainUiState(
    val organisations: List<OrganisationItem> = emptyList(),
    val selectedOrganisationId: String? = null,
    val profile: Profile? = null,
)
