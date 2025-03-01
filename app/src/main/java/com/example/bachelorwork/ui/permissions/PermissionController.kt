package com.example.bachelorwork.ui.permissions

import androidx.appcompat.app.AppCompatActivity

interface PermissionController {

    suspend fun requestPermission(permission: AppPermission)

    suspend fun requestPermissions(permissions: List<AppPermission>)

    fun registerForActivityResult(activity: AppCompatActivity)

}