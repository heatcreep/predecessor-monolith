package com.aowen.monolith.network

import retrofit2.HttpException
import retrofit2.Response

suspend fun <T, R> safeApiCall(
    apiCall: suspend () -> Response<T>,
    transform: suspend (T) -> R,
): Resource<R> =
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Resource.Success(transform(it))
            } ?: Resource.NetworkError(null, "response body was null")
        } else {
            Resource.NetworkError(
                response.code(),
                "Failed to fetch data. (Code: ${response.code()} - ${response.errorBody()?.string()})"
            )
        }
    } catch (throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                val code = throwable.code()
                val error = throwable.response()?.message().toString()
                Resource.NetworkError(code, error)
            }

            else -> {
                Resource.GenericError(throwable.message)
            }
        }
    }

