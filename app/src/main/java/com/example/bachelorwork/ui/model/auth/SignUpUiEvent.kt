package com.example.bachelorwork.ui.model.auth

sealed class SignUpUiEvent {
    data class CompanyNameChanged(val name: String) : SignUpUiEvent()
    data class FullNameChanged(val fullName: String) : SignUpUiEvent()
    data class EmailChanged(val email: String) : SignUpUiEvent()
    data class PasswordChanged(val password: String) : SignUpUiEvent()
    data class PasswordConfirmChanged(val password: String) : SignUpUiEvent()

    data object NavigateBack : SignUpUiEvent()
    data object AgreeProcessingPersonalDataChanged : SignUpUiEvent()
    data object SignUp : SignUpUiEvent()
}