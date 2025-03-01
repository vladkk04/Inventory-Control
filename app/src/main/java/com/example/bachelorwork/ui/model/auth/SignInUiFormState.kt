package com.example.bachelorwork.ui.model.auth

data class SignInUiFormState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
)
