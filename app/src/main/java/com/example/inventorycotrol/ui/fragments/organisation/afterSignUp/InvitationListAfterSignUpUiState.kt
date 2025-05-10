package com.example.inventorycotrol.ui.fragments.organisation.afterSignUp

import com.example.inventorycotrol.domain.model.profile.OrganisationInvitation


data class InvitationListAfterSignUpUiState(
    val invitations: List<OrganisationInvitation> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
)