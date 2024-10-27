package com.example.bachelorwork.ui.common.base

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(requireContext(), message, duration).show()