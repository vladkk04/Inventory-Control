package com.example.bachelorwork.domain.model.validator.validators

import com.example.bachelorwork.domain.model.validator.InputFieldValidator
import com.example.bachelorwork.domain.model.validator.ValidationResult

object ValidatorDecimalFormat: InputFieldValidator {
    override operator fun invoke(input: String): ValidationResult {
        val hasError =  input.endsWith('.')

        return ValidationResult(
            hasError,
            if (hasError) "Invalid number format" else null
        )
    }
}