package com.example.bachelorwork.domain.usecase.validators

import com.example.bachelorwork.domain.model.InputFieldValidator
import com.example.bachelorwork.domain.model.ValidationResult

object ValidatorPercentage: InputFieldValidator {
    override fun invoke(input: String): ValidationResult {
        val hasError = input.toDouble() > 100

        return ValidationResult(
            success = hasError,
            errorMessage = if (hasError) "The percentage cannot be greater than 100%" else null
        )
    }
}