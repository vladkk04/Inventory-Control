package com.example.bachelorwork.ui.model.auth.forgotPassword

sealed class ForgotPasswordUiEvent {
    data class EmailChanged(val email: String) : ForgotPasswordUiEvent()
    data object NavigateBack : ForgotPasswordUiEvent()
    data object Send : ForgotPasswordUiEvent()
}