package com.example.bachelorwork.ui.utils.snackbar

import android.view.Gravity
import androidx.annotation.GravityInt
import com.google.android.material.snackbar.Snackbar

data class SnackbarAppearanceConfig(
    val duration: Int = Snackbar.LENGTH_LONG,
    @GravityInt val gravity: Int = Gravity.CENTER,
)