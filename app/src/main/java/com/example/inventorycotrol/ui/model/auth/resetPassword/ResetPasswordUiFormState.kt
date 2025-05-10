package com.example.inventorycotrol.ui.model.auth.resetPassword

data class ResetPasswordUiFormState(
    val password: String = "",
    val confirmPassword: String = "",
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)
