package com.example.bachelorwork.ui.utils.dialogs

import androidx.fragment.app.Fragment
import com.example.bachelorwork.R
import com.example.bachelorwork.ui.common.base.BaseDialog

fun Fragment.createDeleteDialog(
    deleteItemTitle: String,
    onPositiveButtonClick: () -> Unit = {},
)= object: BaseDialog(requireContext()) {
    override val config: DialogConfig
        get() = DialogConfig(
            title = requireContext().getString(R.string.text_delete_item, deleteItemTitle),
            message = requireContext().getString(R.string.text_delete_item_confirm, deleteItemTitle),
            positiveButtonText = "Delete",
            positiveButtonAction = onPositiveButtonClick,
        )
}