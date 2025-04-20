package com.example.bachelorwork.common

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error <T> (val data: T? = null, val errorMessage: String) : Resource<T>()
}