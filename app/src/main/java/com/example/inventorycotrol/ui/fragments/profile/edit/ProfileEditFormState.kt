package com.example.inventorycotrol.ui.fragments.profile.edit

import android.net.Uri

data class ProfileEditFormState(
    val logo: Uri? = null,
    val fullName: String = "",
    val fullNameError: String? = null,
)
