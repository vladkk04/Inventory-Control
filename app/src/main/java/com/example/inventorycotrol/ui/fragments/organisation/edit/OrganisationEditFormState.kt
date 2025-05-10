package com.example.inventorycotrol.ui.fragments.organisation.edit

import android.net.Uri

data class OrganisationEditFormState(
    val logo: Uri? = null,
    val organisationName: String = "",
    val organisationError: String? = null,
    val description: String = "",
    val currency: String = "",
)
