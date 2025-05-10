package com.example.inventorycotrol.ui.model.auth.signUp

data class SignUpUiFormState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val agreeProcessingPersonalData: Boolean = false,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)
