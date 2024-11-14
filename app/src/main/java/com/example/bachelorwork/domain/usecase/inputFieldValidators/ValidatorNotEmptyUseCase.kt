package com.example.bachelorwork.domain.usecase.inputFieldValidators

import com.example.bachelorwork.domain.model.ValidationResult
import javax.inject.Inject

class ValidatorNotEmptyUseCase @Inject constructor() {
    operator fun invoke(value: String): ValidationResult {
        val hasError = value.isEmpty()

        return ValidationResult(
            hasError,
            if (hasError) "This field cannot be empty" else null
        )
    }
}