package com.example.bachelorwork.ui.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.bachelorwork.ui.common.AppDialogs
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import kotlin.coroutines.suspendCoroutine

class AppPermissionImpl(
    private val applicationContext: Context,
) : PermissionController {

    private val mutex = Mutex()

    private var activity: AppCompatActivity? = null

    private var activityResultLauncher: ActivityResultLauncher<Array<String>>? = null

    override suspend fun requestPermission(permission: AppPermission) {
        mutex.withLock {
            suspendCoroutine { continuation ->
                continuation.resumeWith(runCatching {
                    activityResultLauncher?.launch(permission.permission)
                })
            }
        }
    }

    override suspend fun requestPermissions(permissions: List<AppPermission>) {
        mutex.withLock {
            suspendCoroutine { continuation ->
                continuation.resumeWith(runCatching {
                    activityResultLauncher?.launch(permissions.flatMap { it.permission.toList() }.toTypedArray())
                })
            }
        }
    }

    override fun registerForActivityResult(activity: AppCompatActivity) {
        this.activity = activity

        val activityResultLauncher = activity.activityResultRegistry

        this@AppPermissionImpl.activityResultLauncher = activityResultLauncher.register(
            UUID.randomUUID().toString(),
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            val success = result.any { it.value }

            if (!success) {
                AppDialogs.createPermissionDialog(
                    activity,
                    AppPermission.MEDIA_IMAGES,
                    shouldShowRequestPermissionRationale(activity, result.keys.first()),
                    onPositiveButtonClick = {
                        activity.lifecycleScope.launch { requestPermission(AppPermission.MEDIA_IMAGES) }
                    },
                    onGoToAppSettingsClick = { openAppSettings() }
                ).show()
            }
        }

        activity.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_DESTROY -> {
                        this@AppPermissionImpl.activityResultLauncher = null
                        this@AppPermissionImpl.activity = null
                        source.lifecycle.removeObserver(this)
                    }

                    else -> { return }
                }
            }
        }
        )
    }


    private fun openAppSettings() {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", applicationContext.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        applicationContext.startActivity(intent)
    }
}