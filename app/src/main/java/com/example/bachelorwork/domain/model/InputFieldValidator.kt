package com.example.bachelorwork.domain.model

interface InputFieldValidator {
    operator fun invoke(input: String): ValidationResult
}