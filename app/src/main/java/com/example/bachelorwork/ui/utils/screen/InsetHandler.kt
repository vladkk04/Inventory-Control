package com.example.bachelorwork.ui.utils.screen

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

object InsetHandler {
    fun adaptToEdgeWithMargin(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.statusBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            (v.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(
                    bars.left,
                    bars.top,
                    bars.right,
                    bars.bottom
                )
                v.layoutParams = this
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    fun adaptToEdgeWithPadding(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.statusBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            Log.d("debug", v.paddingTop.toString())

            WindowInsetsCompat.CONSUMED
        }
    }
}
