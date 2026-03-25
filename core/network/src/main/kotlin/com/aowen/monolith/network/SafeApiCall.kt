package com.aowen.monolith.network

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

suspend fun <R> safeApiCallsConcurrent(
    vararg apiCallsWithTransforms: Pair<suspend () -> Response<*>, suspend (Any?) -> Any?>,
    combineResults: suspend (List<Any?>) -> R
): Resource<R> = coroutineScope {
    try {
        // Launch all API calls concurrently
        val deferredResults = apiCallsWithTransforms.map { (apiCall, transform) ->
            async {
                val response = apiCall()
                if (response.isSuccessful) {
                    response.body()?.let {
                        transform(it)
                    } ?: throw IllegalStateException("Response body was null")
                } else {
                    throw HttpException(response)
                }
            }
        }

        // Await all results
        val results = deferredResults.map { it.await() }

        // Combine all results using the provided function
        Resource.Success(combineResults(results))
    } catch (throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                val code = throwable.code()
                val error = throwable.response()?.message().toString()
                Resource.NetworkError(code, error)
            }
            is IllegalStateException -> {
                Resource.NetworkError(null, throwable.message ?: "Response body was null")
            }
            else -> {
                Resource.GenericError(throwable.message)
            }
        }
    }
}

