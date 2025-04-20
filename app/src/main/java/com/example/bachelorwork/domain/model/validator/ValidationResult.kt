package com.example.bachelorwork.domain.model.validator

data class ValidationResult(
    val hasError: Boolean,
    val errorMessage: String? = null
)