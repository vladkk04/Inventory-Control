package com.example.bachelorwork.domain.usecase.inputFieldValidation

data class ValidationResult(
    val success: Boolean,
    val errorMessage: String? = null
)