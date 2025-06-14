package com.example.clima.view.home

sealed interface ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>
    data class Error(val exception: Throwable) : ResultWrapper<Nothing>
}
