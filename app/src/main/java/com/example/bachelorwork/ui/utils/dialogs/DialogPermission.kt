package com.example.bachelorwork.ui.utils.dialogs

import androidx.fragment.app.Fragment
import com.example.bachelorwork.ui.permission.PermissionTextProvider

fun Fragment.createDialogPermission(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onGoToAppSettingsClick: () -> Unit,
) = object : BaseDialog(requireContext()) {
    override val config: DialogConfig
        get() = DialogConfig(
            title = "Permission required",
            message = permissionTextProvider.getDescription(isPermanentlyDeclined),
            positiveButtonText = if (isPermanentlyDeclined) "OK" else "Settings",
            positiveButtonAction = {
                if (isPermanentlyDeclined) {
                    onGoToAppSettingsClick()
                }
            }
        )
}