package com.aowen.predcompanion.core.data.repository.auth

import com.aowen.predcompanion.core.network.AuthTokenProvider
import javax.inject.Inject

class PredGgAuthTokenProvider @Inject constructor(
    private val authRepository: AppAuthRepository,
): AuthTokenProvider {
    override suspend fun freshToken(): String? = authRepository.freshAccessToken()
}