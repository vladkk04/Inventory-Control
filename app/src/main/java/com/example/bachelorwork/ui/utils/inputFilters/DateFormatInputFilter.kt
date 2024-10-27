package com.example.bachelorwork.ui.utils.inputFilters

import android.text.InputFilter
import android.text.Spanned
import com.example.bachelorwork.ui.constant.Constants

object DateFormatInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val newInput = StringBuilder(dest.toString()).apply {
            replace(dstart, dend, source?.subSequence(start, end).toString())
        }

        if (newInput.length >= 11 || newInput.isEmpty()) { return "" }

        return if (Constants.DEFAULT_DATE_FORMAT_PATTERN[newInput.lastIndex] == '/' && newInput.last() != '/') { "/" }
        else { source ?: "" }
    }
}