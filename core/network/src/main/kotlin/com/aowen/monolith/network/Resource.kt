package com.aowen.monolith.network

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class NetworkError(val code: Int? = null, val errorMessage: String? = null): Resource<Nothing>()
    data class GenericError(val errorMessage: String?) : Resource<Nothing>()
}

fun <T> Resource<T>.getOrThrow(): T {
    return when (this) {
        is Resource.Success<T> -> data
        is Resource.NetworkError -> throw Exception("Network error: ${errorMessage ?: "Unknown error"} (Code: ${code})")
        is Resource.GenericError -> throw Exception("Something went wrong: $errorMessage")
    }
}