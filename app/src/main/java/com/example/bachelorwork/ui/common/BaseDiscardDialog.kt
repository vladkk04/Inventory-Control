package com.example.bachelorwork.ui.common

import androidx.fragment.app.Fragment

fun Fragment.showDiscardDialog(
    title: String = "Discard Changes",
    message: String = "Are you sure you want to discard your changes?",
    positiveButtonText: String = "Discard",
    negativeButtonText: String = "Cancel",
    positiveButtonAction: (() -> Unit)? = null,
    negativeButtonAction: (() -> Unit)? = null
) {
    showDialog(
        title = title,
        message = message,
        positiveButtonText = positiveButtonText,
        negativeButtonText = negativeButtonText,
        positiveButtonAction = positiveButtonAction,
        negativeButtonAction = negativeButtonAction
    )
}