package com.example.bachelorwork.ui.model.organisation

data class CreateOrganisationUiFormState(
    val organisationName: String = "",
    val organisationError: String? = null,
    val description: String = "",
    val currency: String = "",
    val currencyError: String? = null,
)