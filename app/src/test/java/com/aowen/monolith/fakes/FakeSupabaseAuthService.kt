package com.aowen.monolith.fakes

import com.aowen.monolith.network.SupabaseAuthService
import io.github.jan.supabase.gotrue.user.AppMetadata
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserSession
import kotlinx.datetime.Instant
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeSupabaseAuthService(private val resCode: Int? = null) : SupabaseAuthService {

    companion object {
        val fakeUserSession = UserSession(
            accessToken = "fakeAccessToken",
            refreshToken = "fakeRefreshToken",
            providerRefreshToken = "fakeProviderRefreshToken",
            providerToken = "fakeProviderToken",
            expiresIn = 3000,
            tokenType = "fakeTokenType",
            user = UserInfo(
                id = "fakeId",
                appMetadata = AppMetadata(
                    provider = "fakeProvider",
                    providers = listOf("fakeRole")
                ),
                aud = "fakeAud",
            ),
            type = "fakeType",
            expiresAt = Instant.DISTANT_FUTURE,
        )
    }
    override suspend fun loginWithDiscord(): Response<Unit> {
        return when (resCode) {
            408 -> Response.error(408, "Request Timeout".toResponseBody(null))
            400 -> Response.error(400, "Bad Request".toResponseBody(null))
            500 -> Response.error(500, "Bad Request".toResponseBody(null))
            else -> Response.success(Unit)
        }
    }

    override suspend fun currentSession(): UserSession? {
        return when (resCode) {
            200 -> fakeUserSession
            else -> null
        }
    }
}