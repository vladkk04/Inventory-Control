package com.example.bachelorwork.ui.utils.extensions

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged

fun <T: Any> EditText.textChanged(event: (T) -> Unit) {
    doAfterTextChanged { text ->
        event.invoke(text as T)
    }
}