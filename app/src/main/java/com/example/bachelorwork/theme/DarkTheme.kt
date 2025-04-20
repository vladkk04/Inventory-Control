package com.example.bachelorwork.theme

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.bachelorwork.R

class DarkTheme(override val context: Context) : CustomAppTheme {
    override fun id(): Int = 0

    override fun colorBackground(): Int = ContextCompat.getColor(context, R.color.colorBackground)

}