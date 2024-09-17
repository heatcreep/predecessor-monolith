package com.aowen.monolith.fakes

import com.aowen.monolith.network.SupabaseAuthService
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

abstract class SessionStatusScenario {
    object Authenticated : SessionStatusScenario()
    object NotAuthenticated : SessionStatusScenario()
}

class FakeSupabaseAuthService(
    private val resCode: Int? = null,
    private val sessionStatusScenario: SessionStatusScenario? = SessionStatusScenario.Authenticated,
) : SupabaseAuthService {

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
                appMetadata = JsonObject(
                    mapOf(
                        "provider" to JsonPrimitive("fakeProvider"),
                        "roles" to JsonPrimitive("fakeRoles"),
                    )
                ),
                aud = "fakeAud",
                email = "fakeEmail",
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

    override suspend fun awaitAuthService(): StateFlow<SessionStatus> {
        return when(sessionStatusScenario) {
            SessionStatusScenario.Authenticated -> MutableStateFlow(SessionStatus.Authenticated(fakeUserSession))
            else -> MutableStateFlow(SessionStatus.NotAuthenticated(isSignOut = false))
        }
    }

    override suspend fun currentAccessToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun currentSession(): UserSession? {
        return when (resCode) {
            200 -> fakeUserSession
            else -> null
        }
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    override suspend fun refreshCurrentSession() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserAccount(userId: String): Response<ResponseBody> {
        return when (resCode) {
            200 -> Response.success(200, "OK".toResponseBody(null))
            else -> Response.error(400, "Bad Request".toResponseBody())
        }
    }

    override suspend fun getUser(token: String): UserInfo? {
        TODO("Not yet implemented")
    }
}