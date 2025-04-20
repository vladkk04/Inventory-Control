package com.example.bachelorwork.ui.fragments.organisation

import com.example.bachelorwork.domain.model.organisation.Organisation


data class OrganisationListUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val organisations: List<Organisation> = emptyList()
)