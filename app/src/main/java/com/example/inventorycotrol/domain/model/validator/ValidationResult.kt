package com.example.inventorycotrol.domain.model.validator

data class ValidationResult(
    val hasError: Boolean,
    val errorMessage: String? = null
)