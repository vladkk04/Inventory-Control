package com.example.inventorycotrol.theme

import android.content.Context
import com.dolatkia.animatedThemeManager.AppTheme

interface CustomAppTheme: AppTheme {

    val context: Context

    fun colorBackground(): Int

}