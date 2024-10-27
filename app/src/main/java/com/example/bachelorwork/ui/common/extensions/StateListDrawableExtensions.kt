package com.example.bachelorwork.ui.common.extensions

import android.content.Context
import android.graphics.drawable.StateListDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun StateListDrawable.createStateListDrawableChecked(
    context: Context,
    @DrawableRes drawableResIdPositive: Int,
    @DrawableRes drawableResIdNegative: Int,
): StateListDrawable =
    this.apply {
        addState(
            intArrayOf(android.R.attr.state_checked),
            ContextCompat.getDrawable(context, drawableResIdPositive)
        )
        addState(
            intArrayOf(-android.R.attr.state_checked),
            ContextCompat.getDrawable(context, drawableResIdNegative)
        )
    }
