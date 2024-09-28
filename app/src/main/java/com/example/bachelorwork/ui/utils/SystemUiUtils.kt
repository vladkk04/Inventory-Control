package com.example.bachelorwork.ui.utils

import android.app.Activity
import android.view.WindowManager
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.changeStatusBarColor(color: Int) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color
}