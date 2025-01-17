package com.example.bachelorwork.ui.utils.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext

private fun <T> Result<T>.handleResult(
    onSuccess: ((T) -> Unit)? = null,
    onFailure: ((Throwable) -> Unit)? = null
) {
    fold(
        onSuccess = { onSuccess?.invoke(it) },
        onFailure = { e -> onFailure?.invoke(e) }
    )
}

private fun <T> Flow<Result<T>>.handleResult(
    scope: CoroutineScope,
    onSuccess: ((T) -> Unit)? = null,
    onFailure: ((Throwable) -> Unit)? = null
) {
    onEach { result ->
        result.handleResult(onSuccess, onFailure)
    }.launchIn(scope)
}


fun <T> ViewModel.handleResult(
    flowResult: Flow<Result<T>>,
    onSuccess: ((T) -> Unit)? = null,
    onFailure: ((Throwable) -> Unit)? = null,
    coroutineContext: CoroutineContext? = null
) {
    val scope =
        if (coroutineContext != null) viewModelScope.plus(coroutineContext) else viewModelScope
    flowResult.handleResult(scope, onSuccess, onFailure)
}

fun <T> ViewModel.handleResult(
    result: Result<T>,
    onSuccess: ((T) -> Unit)? = null,
    onFailure: ((Throwable) -> Unit)? = null
) {
    result.handleResult(onSuccess, onFailure)
}

