package com.example.inventorycotrol.ui.fragments.organisation

import com.example.inventorycotrol.domain.model.organisation.Organisation


data class OrganisationListUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val organisations: List<Organisation> = emptyList()
)