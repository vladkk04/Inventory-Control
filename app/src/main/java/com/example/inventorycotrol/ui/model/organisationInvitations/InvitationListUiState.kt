package com.example.inventorycotrol.ui.model.organisationInvitations

import com.example.inventorycotrol.domain.model.profile.OrganisationInvitation


data class InvitationListUiState(
    val invitations: List<OrganisationInvitation> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
)