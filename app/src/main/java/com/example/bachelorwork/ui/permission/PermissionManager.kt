package com.example.bachelorwork.ui.permission

interface PermissionManager {

    fun requestPermission(permission: AppPermissions)

    fun requestPermissions(permissions: List<AppPermissions>)

}