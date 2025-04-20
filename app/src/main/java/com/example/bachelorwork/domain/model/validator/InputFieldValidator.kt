package com.example.bachelorwork.domain.model.validator

import android.util.Patterns

fun interface InputFieldValidator {
    operator fun invoke(input: String): ValidationResult
}

class InputValidator private constructor(
    private val validators: List<(String) -> ValidationResult> = listOf()
) {
    private fun customValidation(validator: (String) -> Boolean, errorMessage: String): InputValidator {
        return InputValidator(validators + { input ->
            if (validator(input)) ValidationResult(true, errorMessage) else ValidationResult(false, null)
        })
    }

    fun withNotEmpty(errorMessage: String = "This field cannot be empty") =
        customValidation({ it.isEmpty() }, errorMessage)

    fun withEmail(errorMessage: String = "Invalid email") =
        customValidation({ !Patterns.EMAIL_ADDRESS.matcher(it).matches() }, errorMessage)

    fun withOtp(errorMessage: String = "Invalid otp") =
        customValidation({ it.length != 6 }, errorMessage)

    fun withGreaterThenZero(errorMessage: String = "Invalid value") =
        customValidation({ it.toDouble() <= 0 }, errorMessage)

    fun withPercentage(errorMessage: String = "Invalid value") =
        customValidation({ it.toDouble() > 100 }, errorMessage)

    fun withNotGreaterThenValue(value: Double, errorMessage: String = "Invalid value") =
        customValidation({ it.toDouble() > value }, errorMessage)


    fun build(): InputFieldValidator = InputFieldValidator { input ->
        validators.firstOrNull { it(input).hasError }?.invoke(input) ?: ValidationResult(false, null)
    }

    fun withPasswordConfirmation(
        password: String,
        confirmationPassword: String,
        errorMessage: String = "Passwords do not match"
    ): InputValidator {
        return customValidation({ password != confirmationPassword }, errorMessage)
    }

    companion object {
        fun create() = InputValidator()
    }
}