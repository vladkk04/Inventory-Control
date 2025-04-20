package com.example.bachelorwork.ui.common

import android.content.Context
import com.example.bachelorwork.R
import com.example.bachelorwork.ui.common.base.BaseDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker

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

    fun createCancelDialog(
        context: Context,
        onPositiveButtonClick: () -> Unit = {}
    ) = object : BaseDialog(context) {
        override val config: DialogConfig
            get() = DialogConfig(
                title = "Cancel?",
                message = "Are you sure you want to cancel this action?",
                positiveButtonText = "Cancel",
                negativeButtonText = "Discard",
                positiveButtonAction = onPositiveButtonClick
            )
    }

    fun createSignOutDialog(
        context: Context,
        onPositiveButtonClick: () -> Unit = {}
    ) = object : BaseDialog(context) {
        override val config: DialogConfig
            get() = DialogConfig(
                title = "Sign out?",
                message = "Do you really want to sign out?",
                positiveButtonText = "Sign out",
                negativeButtonText = "Cancel",
                positiveButtonAction = onPositiveButtonClick
            )
    }

    fun createShowDatePicker(
        onPositiveButtonClick: (Long) -> Unit,
    ) =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select data")
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now()).build()
            )
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
            .apply {
                addOnPositiveButtonClickListener { time ->
                    onPositiveButtonClick(time)
                }
            }
}