package com.example.bachelorwork.ui.utils.menu

import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment

fun Fragment.createPopupMenu(
    anchorView: View,
    @MenuRes menuRes: Int,
) = PopupMenu(requireContext(), anchorView).apply {
    menuInflater.inflate(menuRes, this.menu)
}
