package com.example.bachelorwork.ui.common

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.showDialog(
    title: String? = null,
    message: String? = null,
    icon: Drawable? = null,
    @DrawableRes iconRes: Int? = null,
    @LayoutRes viewLayoutResId: Int? = null,
    view: View? = null,
    theme: Int? = null,
    customTitleView: View? = null,
    positiveButtonText: String = "",
    negativeButtonText: String = "Cancel",
    neutralButtonText: String = "",
    positiveButtonAction: (() -> Unit)? = null,
    negativeButtonAction: (() -> Unit)? = null,
    neutralButtonAction: (() -> Unit)? = null
) {
    MaterialAlertDialogBuilder(
        requireContext(),
        theme ?: com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog
    )
        .setTitle(title)
        .setMessage(message)
        .setView(view ?: viewLayoutResId?.let { View.inflate(requireContext(), it, null) })
        .setIcon(icon ?: iconRes?.let { ContextCompat.getDrawable(requireContext(), it) })
        .setCustomTitle(customTitleView)
        .apply {
            setNeutralButton(neutralButtonText.takeIf { it.isNotBlank() }) { _, _ ->
                neutralButtonAction?.invoke()
            }
            setNegativeButton(negativeButtonText.takeIf { it.isNotBlank() }) { _, _ ->
                negativeButtonAction?.invoke()
            }
            setPositiveButton(positiveButtonText.takeIf { it.isNotBlank() }) { _, _ ->
                positiveButtonAction?.invoke()
            }
        }.show()
}