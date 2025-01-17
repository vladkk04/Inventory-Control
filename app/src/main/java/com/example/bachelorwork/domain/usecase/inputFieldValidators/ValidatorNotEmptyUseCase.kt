package com.example.bachelorwork.domain.usecase.inputFieldValidators

import com.example.bachelorwork.domain.model.InputFieldValidator
import com.example.bachelorwork.domain.model.ValidationResult

object ValidatorNotEmptyUseCase: InputFieldValidator {
    override operator fun invoke(input: String): ValidationResult {
        val hasError = input.isEmpty()

        return ValidationResult(
            hasError,
            if (hasError) "This field cannot be empty" else null
        )
    }
}