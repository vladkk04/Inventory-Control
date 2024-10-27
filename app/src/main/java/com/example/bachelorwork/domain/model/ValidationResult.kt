package com.example.bachelorwork.domain.model

data class ValidationResult(
    val success: Boolean,
    val errorMessage: String? = null
)