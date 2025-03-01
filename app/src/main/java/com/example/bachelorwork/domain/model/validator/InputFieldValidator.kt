package com.example.bachelorwork.domain.model.validator

interface InputFieldValidator {
    operator fun invoke(input: String): ValidationResult
}