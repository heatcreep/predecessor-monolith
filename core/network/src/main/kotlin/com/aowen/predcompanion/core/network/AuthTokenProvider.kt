package com.aowen.predcompanion.core.network

interface AuthTokenProvider {
    /** A valid bearer token, refreshed if needed; null if unauthenticated. */
    suspend fun freshToken(): String?
}