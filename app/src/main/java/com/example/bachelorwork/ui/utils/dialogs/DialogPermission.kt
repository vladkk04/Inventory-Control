package com.example.bachelorwork.ui.utils.dialogs

import android.content.Context
import com.example.bachelorwork.ui.common.base.BaseDialog

fun showDialogPermission(
    context: Context,
    isPermissionGranted: Boolean
) = object: BaseDialog(context) {
    override val config: DialogConfig
        get() = DialogConfig(
            title = "Permission required",
            message = if (isPermissionGranted) "Permission granted" else "Permission denied",
            positiveButtonText = if (isPermissionGranted) "OK" else "Settings"
        )

}