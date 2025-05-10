package com.example.inventorycotrol.theme

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeManager

abstract class CustomThemeFragment(
    @LayoutRes private val themeId: Int
): Fragment(themeId) {

    override fun onResume() {
        getThemeManager()?.getCurrentLiveTheme()?.observe(this) {
            syncTheme(it)
        }

        super.onResume()
    }

    protected fun getThemeManager() : ThemeManager? {
        return ThemeManager.instance
    }

    // to sync ui with selected theme
    abstract fun syncTheme(appTheme: AppTheme)
}