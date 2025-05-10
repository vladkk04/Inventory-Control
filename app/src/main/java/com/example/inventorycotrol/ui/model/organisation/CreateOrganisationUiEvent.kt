package com.example.inventorycotrol.ui.model.organisation

import android.net.Uri

sealed class CreateOrganisationUiEvent {
    data class LogoChanged(val uri: Uri?) : CreateOrganisationUiEvent()
    data class OrganisationNameChanged(val name: String) : CreateOrganisationUiEvent()
    data class CurrencyChanged(val currency: String) : CreateOrganisationUiEvent()
    data class DescriptionChanged(val description: String) : CreateOrganisationUiEvent()
    data object Create : CreateOrganisationUiEvent()
}