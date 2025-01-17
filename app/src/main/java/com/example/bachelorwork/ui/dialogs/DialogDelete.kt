package com.example.bachelorwork.ui.dialogs

import android.content.Context
import com.example.bachelorwork.R
import com.example.bachelorwork.ui.common.base.BaseDialog

fun createDeleteDialog(
    context: Context,
    deleteItemTitle: String,
    onPositiveButtonClick: (() -> Unit)? = null,
)= object: BaseDialog(context) {
    override val config: DialogConfig
        get() = DialogConfig(
            title = context.getString(R.string.text_delete_item, deleteItemTitle),
            message = context.getString(R.string.text_delete_item_confirm, deleteItemTitle),
            positiveButtonText = "Delete",
            positiveButtonAction = onPositiveButtonClick,
        )
}