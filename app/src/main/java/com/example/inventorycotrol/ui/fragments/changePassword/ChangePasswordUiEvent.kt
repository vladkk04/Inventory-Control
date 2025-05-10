package com.example.inventorycotrol.ui.fragments.changePassword

sealed class ChangePasswordUiEvent {
    data class NewPassword(val newPassword: String) : ChangePasswordUiEvent()
    data class ConfirmPassword(val confirmPassword: String) : ChangePasswordUiEvent()
    data object ChangePassword : ChangePasswordUiEvent()
}
