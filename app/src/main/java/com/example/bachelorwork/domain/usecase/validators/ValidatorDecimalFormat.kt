package com.example.bachelorwork.domain.usecase.validators

import com.example.bachelorwork.domain.model.InputFieldValidator
import com.example.bachelorwork.domain.model.ValidationResult

object ValidatorDecimalFormat: InputFieldValidator {
    override operator fun invoke(input: String): ValidationResult {
        val hasError =  input.endsWith('.')

        return ValidationResult(
            hasError,
            if (hasError) "Invalid number format" else null
        )
    }
}