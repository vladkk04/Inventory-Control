package com.example.inventorycotrol.ui.fragments.profile.edit

import android.net.Uri

sealed class ProfileEditUiEvent {
    data class LogoChanged(val uri: Uri?) : ProfileEditUiEvent()
    data class FullNameChanged(val name: String) : ProfileEditUiEvent()
    data object SaveChanges : ProfileEditUiEvent()
}