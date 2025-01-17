package com.example.bachelorwork.domain.usecase.inputFieldValidators

import com.example.bachelorwork.domain.model.InputFieldValidator
import com.example.bachelorwork.domain.model.ValidationResult

object ValidatorDecimalFormatUseCase: InputFieldValidator {
    override operator fun invoke(input: String): ValidationResult {
        val hasError =  input.endsWith('.')

        return ValidationResult(
            hasError,
            if (hasError) "Invalid number format" else null
        )
    }
}