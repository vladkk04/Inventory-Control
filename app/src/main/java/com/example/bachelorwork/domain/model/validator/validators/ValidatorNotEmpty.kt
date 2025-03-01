package com.example.bachelorwork.domain.model.validator.validators

import com.example.bachelorwork.domain.model.validator.InputFieldValidator
import com.example.bachelorwork.domain.model.validator.ValidationResult

object ValidatorNotEmpty: InputFieldValidator {
    override operator fun invoke(input: String): ValidationResult {
        val hasError = input.isEmpty()

        return ValidationResult(
            hasError,
            if (hasError) "This field cannot be empty" else null
        )
    }
}