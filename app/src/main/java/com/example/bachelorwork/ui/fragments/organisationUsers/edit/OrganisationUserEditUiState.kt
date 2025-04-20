package com.example.bachelorwork.ui.fragments.organisationUsers.edit


data class OrganisationUserEditUiState(
    val organisationUserName: String = "",
    val organisationUserNameError: String? = null,
    val isLoading: Boolean = false
)