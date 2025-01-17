package com.example.bachelorwork.ui.model.user

sealed class UserManageFormEvent {
    data class FullNameChanged(val fullName: String) : UserManageFormEvent()
    data class EmailChanged(val email: String) : UserManageFormEvent()
    data class PasswordChanged(val password: String) : UserManageFormEvent()
}