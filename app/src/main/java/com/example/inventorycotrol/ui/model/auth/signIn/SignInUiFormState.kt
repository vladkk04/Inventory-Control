package com.example.inventorycotrol.ui.model.auth.signIn

data class SignInUiFormState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
)
