package com.example.bachelorwork.domain.model.validator

data class ValidationResult(
    val success: Boolean,
    val errorMessage: String? = null
)