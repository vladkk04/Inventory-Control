package com.example.bachelorwork.ui.utils.snackbar

import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar

fun showCustomSnackbar(
    view: View,
    snackbarEvent: SnackbarEvent,
    snackbarAppearanceConfig: SnackbarAppearanceConfig
) {
    val snackbar = Snackbar.make(view, snackbarEvent.message, snackbarAppearanceConfig.duration).apply {
        snackbarEvent.action?.let { action ->
            this.setAction(action.name) { action.action() }
        }
    }

    val layoutParams = (snackbar.view.layoutParams as? FrameLayout.LayoutParams)

    layoutParams?.gravity = snackbarAppearanceConfig.gravity
    layoutParams?.width = FrameLayout.LayoutParams.WRAP_CONTENT

    snackbar.view.layoutParams = layoutParams

    snackbar.show()
}