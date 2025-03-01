package com.example.bachelorwork.ui.model.auth

data class SignUpUiFormState(
    val companyName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val agreeProcessingPersonalData: Boolean = false,
    val companyNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)
