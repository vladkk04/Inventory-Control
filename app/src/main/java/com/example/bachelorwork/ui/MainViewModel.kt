package com.example.bachelorwork.ui

import androidx.lifecycle.ViewModel
import com.example.bachelorwork.ui.snackbar.SnackbarController.sendSnackbarEvent
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

): ViewModel() {
    private val timeDelay = 2000L
    private var lastBackPressed = 0L

    fun onBackPressed(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (lastBackPressed + timeDelay > currentTime) {
            true
        } else {
            sendSnackbarEvent(SnackbarEvent("Click exit again"))
            lastBackPressed = currentTime
            false
        }
    }

}