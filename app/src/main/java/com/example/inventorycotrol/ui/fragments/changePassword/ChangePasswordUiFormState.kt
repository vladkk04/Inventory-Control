package com.example.inventorycotrol.ui.fragments.changePassword

data class ChangePasswordUiFormState(
    val newPassword: String = "",
    val newPasswordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null
)
