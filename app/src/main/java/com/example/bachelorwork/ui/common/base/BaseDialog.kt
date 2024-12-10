package com.example.bachelorwork.ui.common.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseDialog(
    private val context: Context,
) {
    protected open val config: DialogConfig = DialogConfig()

    data class DialogConfig(
        val title: String? = null,
        val message: String? = null,
        val icon: Drawable? = null,
        @DrawableRes val iconRes: Int? = null,
        @LayoutRes val viewLayoutRes: Int? = null,
        val view: View? = null,
        val theme: Int? = null,
        val customTitleView: View? = null,
        val positiveButtonText: String = "",
        val negativeButtonText: String = "Cancel",
        val neutralButtonText: String = "",
        val positiveButtonAction: (() -> Unit)? = null,
        val negativeButtonAction: (() -> Unit)? = null,
        val neutralButtonAction: (() -> Unit)? = null,
    )

    protected open fun buildDialog() =
        MaterialAlertDialogBuilder(
            context,
            config.theme
                ?: com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog
        )
            .setTitle(config.title)
            .setMessage(config.message)
            .setView(config.view ?: config.viewLayoutRes?.let {
                LayoutInflater.from(context).inflate(it, null)
            })
            .setIcon(config.icon ?: config.iconRes?.let {
                ContextCompat.getDrawable(
                    context,
                    it
                )
            })
            .setCustomTitle(config.customTitleView)
            .apply {
                setNeutralButton(config.neutralButtonText.takeIf { it.isNotBlank() }) { _, _ ->
                    config.neutralButtonAction?.invoke()
                }
                setNegativeButton(config.negativeButtonText.takeIf { it.isNotBlank() }) { _, _ ->
                    config.negativeButtonAction?.invoke()
                }
                setPositiveButton(config.positiveButtonText.takeIf { it.isNotBlank() }) { _, _ ->
                    config.positiveButtonAction?.invoke()
                }
            }.create()


    fun show() { buildDialog().show() }
}




