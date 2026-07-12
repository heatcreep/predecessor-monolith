package com.aowen.predcompanion.core.network.interceptors

import com.aowen.predcompanion.core.network.AuthTokenProvider
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import javax.inject.Inject

class AuthApolloInterceptor @Inject constructor(
    private val tokenProvider: AuthTokenProvider,
) : HttpInterceptor {
    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain,
    ): HttpResponse {
        val token = tokenProvider.freshToken()
        val authed = if (token != null) {
            request.newBuilder().addHeader("Authorization", "Bearer $token").build()
        } else request
        return chain.proceed(authed)
    }
}