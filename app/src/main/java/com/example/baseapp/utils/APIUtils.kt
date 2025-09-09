package com.example.baseapp.utils

import com.example.baseapp.exception.ApiException

suspend inline fun <reified T> safeApiCall(
    crossinline block: suspend () -> T,
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}

inline fun <T> Result<T>.result(
    onSuccess: (T) -> Unit = {},
    onFailure: (code: Int?, message: String) -> Unit = { _, _ ->}
) {
    this.fold(
        onSuccess = { onSuccess(it) },
        onFailure = { throwable ->
            if (throwable is ApiException) {
                onFailure(throwable.code, throwable.message)
            } else {
                onFailure(null, throwable.message ?: "Something went wrong")
            }
        }
    )
}