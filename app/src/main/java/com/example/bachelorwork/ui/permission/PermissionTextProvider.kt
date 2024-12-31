package com.example.bachelorwork.ui.permission

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}