package com.example.bachelorwork.ui.snackbar

import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object SnackbarController {
    private val _events = MutableSharedFlow<SnackbarEvent>(replay = 0)

    fun observeSnackbarEvents(
        lifecycleOwner: LifecycleOwner,
        view: View,
        anchorView: View? = null
    ) {
        lifecycleOwner.collectInLifecycle(
            flow = _events,
            lifecycleState = Lifecycle.State.STARTED,
            dispatcher = Dispatchers.Main.immediate
        ) { event ->
            showCustomSnackbar(
                view = view,
                anchorView = anchorView,
                snackbarEvent = event,
                snackbarAppearanceConfig = event.appearanceConfig
            )
        }
    }

    fun ViewModel.sendSnackbarEvent(event: SnackbarEvent) = viewModelScope.launch {
        _events.emit(event)
    }

    private fun showCustomSnackbar(
        view: View,
        anchorView: View?,
        snackbarEvent: SnackbarEvent,
        snackbarAppearanceConfig: SnackbarAppearanceConfig,
    ) {
        val snackbar =
            Snackbar.make(view, snackbarEvent.message, snackbarAppearanceConfig.duration).apply {
                snackbarEvent.action?.let { action ->
                    this.setAction(action.name) { action.action() }
                }
            }

        when (val layoutParams = snackbar.view.layoutParams) {
            is FrameLayout.LayoutParams -> {
                layoutParams.gravity = snackbarAppearanceConfig.gravity
                layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT
                snackbar.view.layoutParams = layoutParams
            }

            is CoordinatorLayout.LayoutParams -> {
                layoutParams.gravity = snackbarAppearanceConfig.gravity
                layoutParams.width = CoordinatorLayout.LayoutParams.WRAP_CONTENT
                snackbar.view.layoutParams = layoutParams
            }

            else -> {}
        }

        if (anchorView?.isAttachedToWindow == true) {
            snackbar.anchorView = anchorView
        } else {
            snackbar.anchorView = null // Fallback to no anchor
        }

        snackbar.show()
    }
}


