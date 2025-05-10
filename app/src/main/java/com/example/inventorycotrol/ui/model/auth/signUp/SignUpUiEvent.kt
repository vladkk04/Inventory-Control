package com.example.inventorycotrol.ui.model.auth.signUp

sealed class SignUpUiEvent {
    data class FullNameChanged(val fullName: String) : SignUpUiEvent()
    data class EmailChanged(val email: String) : SignUpUiEvent()
    data class PasswordChanged(val password: String) : SignUpUiEvent()
    data class PasswordConfirmChanged(val password: String) : SignUpUiEvent()

    data object NavigateBack : SignUpUiEvent()
    data object NavigateToSignIn : SignUpUiEvent()
    data object SignUp : SignUpUiEvent()
}