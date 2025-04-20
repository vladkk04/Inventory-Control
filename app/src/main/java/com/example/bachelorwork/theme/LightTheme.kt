package com.example.bachelorwork.theme

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.bachelorwork.R

class LightTheme(override val context: Context) : CustomAppTheme {

    override fun id(): Int = 1

    override fun colorBackground(): Int = ContextCompat.getColor(context, R.color.black)

}