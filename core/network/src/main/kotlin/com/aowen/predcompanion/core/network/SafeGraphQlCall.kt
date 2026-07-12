package com.aowen.predcompanion.core.network

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation

suspend fun <D : Operation.Data, R> safeGraphQlCall(
    apiCall: suspend () -> ApolloResponse<D>,
    transform: suspend (D) -> R,
): Resource<R> =
    try {
        val response = apiCall()
        val data = response.data
        if (data != null && response.errors.isNullOrEmpty()) {
            Resource.Success(transform(data))
        } else {
            val errorMessage = response.errors
                ?.joinToString { it.message }
                ?: "GraphQL response data was null"
            Resource.NetworkError(null, errorMessage)
        }
    } catch (throwable: Throwable) {
        Resource.GenericError(throwable.message)
    }
