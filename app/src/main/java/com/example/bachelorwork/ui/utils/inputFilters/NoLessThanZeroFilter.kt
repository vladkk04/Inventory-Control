package com.example.bachelorwork.ui.utils.inputFilters

import android.text.InputFilter
import android.text.Spanned
import android.util.Log

object NoLessThanZeroFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val newInput = StringBuilder(dest.toString())
        newInput.replace(dstart, dend, source?.subSequence(start, end).toString())

        if (newInput.isNotEmpty() && newInput[0] == '0') return ""

        return source ?: ""
    }
}

