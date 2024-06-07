package org.meetcute.network.data

sealed class NetworkResponse<out T> {
    data class Success<out T>(val value: T?) : NetworkResponse<T>()
    data class Failure(val throwable: Throwable?) : NetworkResponse<Nothing>()
    data object Loading : NetworkResponse<Nothing>()
    data object Empty : NetworkResponse<Nothing>()
}