package com.example.bachelorwork.domain.usecase.inputFieldValidation

import com.example.bachelorwork.domain.model.ValidationResult
import javax.inject.Inject

class ValidateNonEmptyFieldUseCase @Inject constructor() {
    operator fun invoke(value: String): ValidationResult {
        val hasError = value.isBlank()

        return ValidationResult(
            hasError,
            if (hasError) "This field cannot be empty" else null
        )
    }
}