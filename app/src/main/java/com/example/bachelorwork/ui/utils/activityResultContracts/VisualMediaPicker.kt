package com.example.bachelorwork.ui.utils.activityResultContracts

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yalantis.ucrop.UCrop
import java.io.File

class VisualMediaPicker(
    private val fragment: Fragment,
) : DefaultLifecycleObserver {

    private var uCropOptions: UCrop.Options = UCrop.Options()

    private val pickVisualMediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

    private lateinit var visualMediaResultContract: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var cropActivityResult: ActivityResultLauncher<Uri>

    private var callback: VisualMediaPickerCallback? = null

    init { fragment.lifecycle.addObserver(this) }

    override fun onCreate(owner: LifecycleOwner) {
        cropActivityResult = fragment.registerForActivityResult(cropResultContract) { uri ->
            callback?.onMediaPicked(uri)
        }

        visualMediaResultContract = fragment.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { cropActivityResult.launch(it) }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        unregisterVisualMediaPicker()
    }

    fun addCallbackResult(callback: VisualMediaPickerCallback) {
        this.callback = callback
    }

    fun setupUCropOptions(options: UCrop.Options) {
        uCropOptions = options
    }

    fun launchVisualMediaPicker() {
        visualMediaResultContract.launch(pickVisualMediaRequest)
    }

    private fun unregisterVisualMediaPicker() {
        visualMediaResultContract.unregister()
    }

    private val cropResultContract = object : ActivityResultContract<Uri, Uri?>() {
        override fun createIntent(context: Context, input: Uri): Intent {
            val extension = context.contentResolver.getType(input)
                ?.substringAfterLast("/") ?: "jpg"
            val tempOutputFile = File(
                context.externalCacheDir,
                "cropped_${System.currentTimeMillis()}.$extension"
            )
            return UCrop.of(input, tempOutputFile.toUri())
                .withOptions(uCropOptions)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return intent?.let { UCrop.getOutput(it) }
        }
    }
}