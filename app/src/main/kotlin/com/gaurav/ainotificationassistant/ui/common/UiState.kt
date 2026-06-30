package com.gaurav.ainotificationassistant.ui.common

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val exception: Exception) : UiState<Nothing>()

    val isLoading: Boolean
        get() = this is Loading

    val data: T?
        get() = (this as? Success)?.data

    val error: Exception?
        get() = (this as? Error)?.exception
}
