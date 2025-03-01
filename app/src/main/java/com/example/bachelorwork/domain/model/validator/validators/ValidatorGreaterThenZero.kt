package com.example.bachelorwork.domain.model.validator.validators

import com.example.bachelorwork.domain.model.validator.InputFieldValidator
import com.example.bachelorwork.domain.model.validator.ValidationResult

object ValidatorGreaterThenZero: InputFieldValidator {
    override fun invoke(input: String): ValidationResult {
        val hasError = input.toDouble() <= 0

        return ValidationResult(
            hasError,
            if (hasError) "This field must be greater then zero" else null
        )
    }
}