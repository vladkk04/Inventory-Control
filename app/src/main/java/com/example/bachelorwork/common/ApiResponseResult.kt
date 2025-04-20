package com.example.bachelorwork.common

sealed class ApiResponseResult<out T> {
    data object Loading : ApiResponseResult<Nothing>()
    data class Success<out T>(val data: T) : ApiResponseResult<T>()
    data class Failure(val errorMessage: String, val code: Int) : ApiResponseResult<Nothing>()
}