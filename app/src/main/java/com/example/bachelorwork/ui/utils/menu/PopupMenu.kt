package com.example.bachelorwork.ui.utils.menu

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes

fun createPopupMenu(
    context: Context,
    anchorView: View,
    @MenuRes menuRes: Int,
) = PopupMenu(context, anchorView).apply {
    menuInflater.inflate(menuRes, this.menu)
}
