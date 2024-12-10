package com.example.bachelorwork.data.repository

import com.example.bachelorwork.domain.repository.PermissionRequester
import com.example.bachelorwork.ui.constant.AppPermissions

class PermissionRequesterImpl(
) : PermissionRequester {
    
   /* private val activityResultRegistry: ActivityResultRegistry =
        (activityContext as? MainActivity)?.activityResultRegistry
            ?: throw IllegalStateException("Activity not found")*/

    override fun requestSinglePermission(
        appPermission: AppPermissions,
        onPermissionGranted: () -> Unit
    ) {
       /* activityResultRegistry.register(
            "SINGLE_PERMISSION",
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

        }.launch(appPermission.permission)*/
    }

    override fun requestMultiplePermissions(
        appPermissions: List<AppPermissions>,
        onPermissionGranted: () -> Unit
    ) {
        /*activityResultRegistry.register(
            "MULTIPLE_PERMISSIONS",
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            if (permissionsMap.all { it.value }) {
                onPermissionGranted()
            }
        }.launch(appPermissions.map { it.permission }.toTypedArray())*/

    }
}