package com.example.bachelorwork.ui.fragments.organisation.afterSignUp

import com.example.bachelorwork.domain.model.profile.OrganisationInvitation


data class InvitationListAfterSignUpUiState(
    val invitations: List<OrganisationInvitation> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
)