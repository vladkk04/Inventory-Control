package com.example.bachelorwork.ui.common

import android.content.Context
import com.example.bachelorwork.R
import com.example.bachelorwork.ui.common.base.BaseDialog
import com.example.bachelorwork.ui.permissions.AppPermission

object AppDialogs {
    fun createDeleteDialog(
        context: Context,
        deleteItemTitle: String,
        onPositiveButtonClick: (() -> Unit)? = null,
    ) = object : BaseDialog(context) {
        override val config: DialogConfig
            get() = DialogConfig(
                title = context.getString(R.string.text_delete_item, deleteItemTitle),
                message = context.getString(R.string.text_delete_item_confirm, deleteItemTitle),
                positiveButtonText = "Delete",
                positiveButtonAction = onPositiveButtonClick,
            )
    }

    fun createDeleteDialog(
        context: Context,
        title: String? = null,
        message: String? = null,
        deleteItemTitle: String? = null,
        onPositiveButtonClick: (() -> Unit)? = null,
    ) = object : BaseDialog(context) {
        override val config: DialogConfig
            get() = DialogConfig(
                title = title ?: context.getString(R.string.text_delete_item, deleteItemTitle),
                message = message,
                positiveButtonText = "Delete",
                positiveButtonAction = onPositiveButtonClick,
            )
    }

    fun createDiscardDialog(
        context: Context,
        onPositiveButtonClick: () -> Unit = {}
    ) = object : BaseDialog(context) {
        override val config: DialogConfig
            get() = DialogConfig(
                title = "Discard draft?",
                message = "Are you sure you want to discard this draft?",
                positiveButtonText = "Discard",
                positiveButtonAction = onPositiveButtonClick
            )
    }

    fun createPermissionDialog(
        context: Context,
        permission: AppPermission,
        isPermanentlyDeclined: Boolean,
        onPositiveButtonClick: () -> Unit = {},
        onGoToAppSettingsClick: () -> Unit = {},
    ) = object : BaseDialog(context) {
        override val config: DialogConfig
            get() = super.config.copy(
                title = getTitle,
                message = getMessage,
                positiveButtonText = if (isPermanentlyDeclined) "Ok" else "Settings",
                positiveButtonAction = if (isPermanentlyDeclined) onPositiveButtonClick else onGoToAppSettingsClick,
                negativeButtonText = if (isPermanentlyDeclined) "" else "Not Now",
            )

        val applicationName = context.applicationInfo.loadLabel(context.packageManager)

        private val getMessage = when (permission) {
            AppPermission.MEDIA_IMAGES -> {
                "$applicationName need access to your Gallery to upload images. Please grant the permission."
            }
        }

        private val getTitle = when (permission) {
            AppPermission.MEDIA_IMAGES -> {
                "Allow your Gallery"
            }
        }
    }

    /*fun createShowDatePicker(
        onPositiveButtonClick: (String) -> Unit,
    ) =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select data")
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now()).build()
            )
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTextInputFormat(SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_PATTERN, Locale.getDefault()))
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    onPositiveButtonClick(
                        SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_PATTERN, Locale.getDefault()).format(it)
                    )
                }
            }
            .show(childFragmentManager, "${this::class.simpleName} DataPicker")*/
}