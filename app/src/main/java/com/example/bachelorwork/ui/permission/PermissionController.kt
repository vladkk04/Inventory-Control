package com.example.bachelorwork.ui.permission

import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.bachelorwork.ui.utils.dialogs.createDialogPermission
import com.example.bachelorwork.util.openAppSetting

object PermissionController: PermissionManager {

    private var fragment: Fragment? = null

    fun init(fragment: Fragment): PermissionController {
        this.fragment = fragment
        return this
    }

    private val requestPermissionLauncher =
        fragment?.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d("debug", "dd")
            fragment?.let {
                it.createDialogPermission(
                    GalleryPermissionTextProvider(),
                    isGranted,
                    it.requireActivity()::openAppSetting
                )
            }
        }

    override fun requestPermission(permission: AppPermissions) {
        Log.d("debug", fragment.toString())
        requestPermissionLauncher?.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
    }


    override fun requestPermissions(permissions: List<AppPermissions>) {

    }

}