package com.example.bachelorwork.ui.common

import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import com.example.bachelorwork.R

fun Fragment.showPopupMenu(
    anchorView: View,
    @MenuRes menuRes: Int,
    setOnMenuItemClickListener: ((menuItem: MenuItem) -> Unit)? = null
) {
    PopupMenu(requireContext(), anchorView).apply {
        menuInflater.inflate(menuRes, this.menu)
        setOnMenuItemClickListener {
            setOnMenuItemClickListener?.invoke(it)
            true
        }
    }.show()
}