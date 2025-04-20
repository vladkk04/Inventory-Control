package com.example.bachelorwork.ui

import com.example.bachelorwork.domain.model.organisation.Organisation
import com.example.bachelorwork.domain.model.user.Profile

data class MainUiState(
    val organisations: List<Organisation> = emptyList(),
    val selectedOrganisationId: String? = null,
    val profile: Profile? = null
)
