package com.example.bachelorwork.ui.utils.recyclerview

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller

enum class ScrollSpeed(val millisecondsPerInch: Float) {
    SUPER_SLOW(300f),
    SLOW(200f),
    MEDIUM(100f),
    FAST(50f),
    VERY_FAST(25f),
    ULTRA_FAST(10f)
}

class SpeedyLinearSmoothScroller(
    context: Context,
    targetPosition: Int = 0,
    private val scrollSpeed: ScrollSpeed = ScrollSpeed.ULTRA_FAST
) : LinearSmoothScroller(context) {

    init {
        setTargetPosition(targetPosition)
    }

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        return scrollSpeed.millisecondsPerInch / displayMetrics.densityDpi
    }
}