package com.example.bachelorwork.ui.views

import android.content.Context
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout.LayoutParams
import androidx.core.content.ContextCompat
import com.example.bachelorwork.domain.model.product.ProductTag
import com.google.android.material.chip.Chip

fun createProductTagChip(context: Context, productTag: ProductTag): Chip {
    return Chip(context).apply {
        text = productTag.name
        layoutParams = MarginLayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply { setMargins(10, 0, 10, 0) }
        chipIcon = ContextCompat.getDrawable(context, productTag.icon)
    }
}