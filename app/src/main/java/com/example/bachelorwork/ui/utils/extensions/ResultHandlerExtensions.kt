package com.example.bachelorwork.ui.utils.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.common.ApiResponseResult
import com.example.bachelorwork.common.ApiResponseResult.Failure
import com.example.bachelorwork.common.ApiResponseResult.Success
import com.example.bachelorwork.common.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import retrofit2.HttpException
import retrofit2.Response
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

fun <T> safeApiCallFlow(
    call: suspend () -> T
): Flow<ApiResponseResult<T>> = flow {
    emit(ApiResponseResult.Loading)
    try {
        val response = call()
        emit(Success(response))
    } catch (e: HttpException) {
        emit(Failure(e.response()?.errorBody()?.string() ?: "Unknown HTTP error", e.code()))
    } catch (e: Exception) {
        emit(Failure(e.message ?: e.toString(), 400))
    }
}.flowOn(Dispatchers.IO)

fun <T : Any, R> performNetworkOperation(
    remoteCall: suspend () -> Flow<ApiResponseResult<T>>,
    localUpdate: suspend (T) -> Unit,
    transform: suspend (T) -> R
): Flow<Resource<R>> = flow {
    emit(Resource.Loading)

    try {
        when (val response = remoteCall().last()) {
            ApiResponseResult.Loading -> Unit // Already emitted Loading
            is Failure -> {
                emit(Resource.Error(errorMessage = response.errorMessage))
            }
            is Success -> {
                localUpdate(response.data)
                emit(Resource.Success(transform(response.data)))
            }
        }
    } catch (e: Exception) {
        emit(Resource.Error(errorMessage = e.message ?: "Unknown error"))
    }
}

fun <T : Any> performNetworkOperation(
    remoteCall: suspend () -> Flow<ApiResponseResult<T>>,
    localUpdate: suspend (T) -> Unit = { _ -> } // Optional local update
): Flow<Resource<Unit>> = flow {
    emit(Resource.Loading)

    try {
        when (val response = remoteCall().last()) {
            ApiResponseResult.Loading -> Unit // Already emitted Loading
            is ApiResponseResult.Failure -> {
                emit(Resource.Error(errorMessage = response.errorMessage))
            }
            is ApiResponseResult.Success -> {
                localUpdate(response.data) // Update local storage if needed
                emit(Resource.Success(Unit)) // Just signal success
            }
        }
    } catch (e: Exception) {
        emit(Resource.Error(errorMessage = e.message ?: "Unknown error"))
    }
}


fun <T> safeResponseApiCallFlow(
    call: suspend () -> Response<T>
): Flow<ApiResponseResult<T>> = flow {
    emit(ApiResponseResult.Loading)
    val response = call()

    try {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                emit(Success(data))
            }
        } else {
            response.errorBody()?.let { error ->
                error.close()
                emit(Failure(error.string(), response.code()))
            }
        }
    } catch (e: Exception) {
        emit(Failure(e.message ?: e.toString(), 400))
    }
}.flowOn(Dispatchers.IO)

