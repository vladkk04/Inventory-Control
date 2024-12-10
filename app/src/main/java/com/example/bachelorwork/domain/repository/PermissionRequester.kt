package com.example.bachelorwork.domain.repository

import com.example.bachelorwork.ui.constant.AppPermissions

interface PermissionRequester {

    fun requestSinglePermission(appPermission: AppPermissions, onPermissionGranted: () -> Unit)

    fun requestMultiplePermissions(appPermissions: List<AppPermissions>, onPermissionGranted: () -> Unit)

}