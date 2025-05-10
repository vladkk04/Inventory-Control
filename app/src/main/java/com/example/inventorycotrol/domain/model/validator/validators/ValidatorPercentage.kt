package com.example.inventorycotrol.domain.model.validator.validators

import com.example.inventorycotrol.domain.model.validator.InputFieldValidator
import com.example.inventorycotrol.domain.model.validator.ValidationResult

object ValidatorPercentage: InputFieldValidator {
    override fun invoke(input: String): ValidationResult {
        val hasError = input.toDouble() > 100

        return ValidationResult(
            hasError = hasError,
            errorMessage = if (hasError) "The percentage cannot be greater than 100%" else null
        )
    }
}