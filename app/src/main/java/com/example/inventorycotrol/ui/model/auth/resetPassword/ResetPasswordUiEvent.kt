package com.example.inventorycotrol.ui.model.auth.resetPassword

sealed class ResetPasswordUiEvent {
    data class PasswordChanged(val password: String) : ResetPasswordUiEvent()
    data class PasswordConfirmChanged(val confirmPassword: String) : ResetPasswordUiEvent()

    data object ResetPassword : ResetPasswordUiEvent()
}