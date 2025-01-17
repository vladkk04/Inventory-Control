package com.example.bachelorwork.domain.model

class ValidatorInputFieldFactory(
    vararg inputs: String,
    private val validators: Set<InputFieldValidator>
) {
    val hasError = inputs.any { input -> validators.any { validator -> validator(input).success } }

    val errorMessages: Map<String, String?> = inputs.associateWith { input ->
        validators.firstNotNullOfOrNull { validator -> validator(input).errorMessage }
    }

}