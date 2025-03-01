package com.example.bachelorwork.ui.model.auth

sealed class SignInUiEvent {
    data class EmailChanged(val email: String) : SignInUiEvent()
    data class PasswordChanged(val password: String) : SignInUiEvent()
    data object NavigateBack : SignInUiEvent()
    data object RememberMeChanged : SignInUiEvent()
    data object ForgotPassword : SignInUiEvent()
    data object SignIn : SignInUiEvent()
}