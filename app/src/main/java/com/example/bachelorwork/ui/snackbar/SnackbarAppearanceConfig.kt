package com.example.bachelorwork.ui.snackbar

import android.view.Gravity
import androidx.annotation.GravityInt
import com.google.android.material.snackbar.Snackbar

data class SnackbarAppearanceConfig(
    val duration: Int = Snackbar.LENGTH_SHORT,
    @GravityInt
    val gravity: Int = Gravity.CENTER,
)