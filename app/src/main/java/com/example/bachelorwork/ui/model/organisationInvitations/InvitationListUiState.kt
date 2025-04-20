package com.example.bachelorwork.ui.model.organisationInvitations

import com.example.bachelorwork.domain.model.profile.OrganisationInvitation


data class InvitationListUiState(
    val invitations: List<OrganisationInvitation> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
)