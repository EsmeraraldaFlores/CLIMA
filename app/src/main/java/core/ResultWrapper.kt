package com.example.clima.repository

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T): ResultWrapper<T>()
    data class Error(val exception: Throwable): ResultWrapper<Nothing>()
}

suspend fun <T> safeCall(action: suspend () -> T): ResultWrapper<T> {
    return try {
        ResultWrapper.Success(action())
    } catch (e: Exception) {
        ResultWrapper.Error(e)
    }
}
