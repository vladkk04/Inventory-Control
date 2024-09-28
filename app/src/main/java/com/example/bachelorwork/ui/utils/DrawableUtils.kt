package com.example.bachelorwork.ui.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.StateListDrawable
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun createStateListDrawableChecked(
    context: Context,
    @DrawableRes drawableResIdPositive: Int,
    @DrawableRes drawableResIdNegative: Int,
): StateListDrawable {
    return StateListDrawable().apply {
        addState(
            intArrayOf(android.R.attr.state_checked),
            ContextCompat.getDrawable(context, drawableResIdPositive)
        )
        addState(
            intArrayOf(-android.R.attr.state_checked),
            ContextCompat.getDrawable(context, drawableResIdNegative)
        )
    }
}