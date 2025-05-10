package com.example.inventorycotrol.domain.model.validator.validators

import com.example.inventorycotrol.domain.model.validator.InputFieldValidator
import com.example.inventorycotrol.domain.model.validator.ValidationResult

object ValidatorPassword: InputFieldValidator {

    override fun invoke(input: String): ValidationResult {
        val hasError = input.length < 8

        return ValidationResult(
            hasError,
            if (hasError) "Password must be at least 8 characters" else null
        )
    }
}