package com.example.bachelorwork.ui.common

import android.provider.CalendarContract.Calendars
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
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
        .build().apply {
            addOnPositiveButtonClickListener {
                onPositiveButtonClick(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it))
            }
        }
        .show(childFragmentManager, "${this::class.simpleName} DataPicker")

