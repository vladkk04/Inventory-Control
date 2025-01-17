package com.example.bachelorwork.ui.model.user

import com.example.bachelorwork.domain.model.user.Role

data class CreateUserUiState(
    val randomPassword: String = "",
    val role: Role = Role.ADMIN,
)
