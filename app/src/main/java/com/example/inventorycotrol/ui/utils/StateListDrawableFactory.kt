package com.example.inventorycotrol.ui.utils

import android.content.Context
import android.graphics.drawable.StateListDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

object StateListDrawableFactory {
    fun createCheckedDrawable(
        context: Context,
        @DrawableRes drawableResIdPositive: Int,
        @DrawableRes drawableResIdNegative: Int
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
}
