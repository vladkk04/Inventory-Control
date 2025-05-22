package com.example.inventorycotrol.domain.model.validator

fun interface InputFieldValidator {
    operator fun invoke(input: String): ValidationResult
}