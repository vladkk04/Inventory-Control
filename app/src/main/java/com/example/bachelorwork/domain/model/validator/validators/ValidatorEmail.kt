package com.example.bachelorwork.domain.model.validator.validators

import android.util.Patterns
import com.example.bachelorwork.domain.model.validator.InputFieldValidator
import com.example.bachelorwork.domain.model.validator.ValidationResult

object ValidatorEmail: InputFieldValidator {
    override fun invoke(input: String): ValidationResult {
        val hasError = !Patterns.EMAIL_ADDRESS.matcher(input).matches()

        return ValidationResult(
            hasError,
            if (hasError) "Invalid email" else null
        )
    }
}