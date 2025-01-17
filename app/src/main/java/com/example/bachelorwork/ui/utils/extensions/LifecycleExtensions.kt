package com.example.bachelorwork.ui.utils.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <T> LifecycleOwner.collectInLifecycle(
    flow: Flow<T>,
    lifecycleState: Lifecycle.State,
    dispatcher: CoroutineContext = EmptyCoroutineContext,
    collector: suspend (T) -> Unit
) {
    lifecycleScope.launch(dispatcher) {
        lifecycle.repeatOnLifecycle(lifecycleState) {
            flow.collectLatest { item -> collector(item) }
        }
    }
}

fun <T> LifecycleOwner.collectInLifecycle(
    flow: Flow<T>,
    dispatcher: CoroutineContext = EmptyCoroutineContext,
    collector: suspend (T) -> Unit
) {
    lifecycleScope.launch(dispatcher) {
        flow.collect { item -> collector(item) }
    }
}