package com.example.bachelorwork.ui.views

import android.content.Context
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout.LayoutParams
import com.example.bachelorwork.domain.model.product.ProductTag
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable


fun createProductTagChip(context: Context, productTag: ProductTag): Chip {
    return Chip(context).apply {
        text = productTag.name
        layoutParams = MarginLayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply { setMargins(10, 0, 10, 0) }
    }
}

fun createOrganisationRolePermissionChip(
    context: Context,
    rolePermissions: String
): Chip {
    return Chip(context).apply {
        setChipDrawable(
            ChipDrawable.createFromAttributes(
                context,
                null,
                0,
                com.google.android.material.R.style.Widget_Material3_Chip_Filter
            )
        )
        text = rolePermissions
    }
}