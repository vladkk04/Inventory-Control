package com.example.bachelorwork.ui.utils.extensions

import android.content.Context


fun Context.dpToPx(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}