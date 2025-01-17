package com.example.bachelorwork.ui.dialogs

import androidx.fragment.app.Fragment
import com.example.bachelorwork.ui.common.base.BaseDialog


fun Fragment.createDiscardDialog(
    onPositiveButtonClick: () -> Unit = {}
) = object : BaseDialog(requireContext()) {
    override val config: DialogConfig
        get() = DialogConfig(
            title = "Discard draft?",
            message = "Are you sure you want to discard this draft?",
            positiveButtonText = "Discard",
            positiveButtonAction = onPositiveButtonClick
        )
}