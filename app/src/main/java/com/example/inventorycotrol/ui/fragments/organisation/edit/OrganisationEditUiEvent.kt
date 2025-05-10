package com.example.inventorycotrol.ui.fragments.organisation.edit

import android.net.Uri

sealed class OrganisationEditUiEvent {
    data class LogoChanged(val uri: Uri?) : OrganisationEditUiEvent()
    data class OrganisationNameChanged(val name: String) : OrganisationEditUiEvent()
    data class CurrencyChanged(val currency: String) : OrganisationEditUiEvent()
    data class DescriptionChanged(val description: String) : OrganisationEditUiEvent()
    data object SaveChanges : OrganisationEditUiEvent()
}