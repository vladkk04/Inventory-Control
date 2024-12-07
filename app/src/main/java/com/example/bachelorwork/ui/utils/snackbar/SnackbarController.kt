package com.example.bachelorwork.ui.utils.snackbar

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.collectInLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object SnackbarController {
    private val _events = Channel<SnackbarEvent>()

    fun observeSnackbarEvents(lifecycleOwner: LifecycleOwner, view: View) {
        lifecycleOwner.collectInLifecycle(
            flow = _events.receiveAsFlow(),
            lifecycleState = Lifecycle.State.STARTED,
            dispatcher = Dispatchers.Main.immediate
        ) { event ->
            showCustomSnackbar(
                view = view,
                snackbarEvent = event,
                snackbarAppearanceConfig = event.appearanceConfig
            )
        }
    }

    fun ViewModel.sendSnackbarEvent(event: SnackbarEvent) = viewModelScope.launch {
        _events.send(event)
    }
}


