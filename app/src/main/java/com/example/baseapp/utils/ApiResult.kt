package com.example.baseapp.utils

sealed class ApiResult<out T> {
    object Loading : ApiResult<Nothing>()
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}
