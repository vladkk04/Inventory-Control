package com.example.bachelorwork.domain.usecase.validators

import com.example.bachelorwork.domain.model.InputFieldValidator
import com.example.bachelorwork.domain.model.ValidationResult

object ValidatorGreaterThenZero: InputFieldValidator  {
    override fun invoke(input: String): ValidationResult {
        val hasError = input.toDouble() <= 0

        return ValidationResult(
            hasError,
            if (hasError) "This field must be greater then zero" else null
        )
    }
}