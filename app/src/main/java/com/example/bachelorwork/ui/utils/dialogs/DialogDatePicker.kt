package com.example.bachelorwork.ui.utils.dialogs

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.bachelorwork.ui.constant.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale

fun Fragment.showDatePicker(
    onPositiveButtonClick: (String) -> Unit,
) =
    MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select data")
        .setCalendarConstraints(
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now()).build()
        )
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .setTextInputFormat(SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_PATTERN, Locale.getDefault()))
        .build()
        .apply {
            addOnPositiveButtonClickListener {
                Log.d("debug", it.toString())
                onPositiveButtonClick(
                    SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_PATTERN, Locale.getDefault()).format(it)
                )
            }
        }
        .show(childFragmentManager, "${this::class.simpleName} DataPicker")