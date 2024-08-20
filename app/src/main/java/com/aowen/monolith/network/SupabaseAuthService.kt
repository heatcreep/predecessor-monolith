package com.aowen.monolith.network

import com.aowen.monolith.BuildConfig
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.providers.Discord
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserSession
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

interface SupabaseAuthService {
    suspend fun currentAccessToken(): String?
    suspend fun currentSession(): UserSession?
    suspend fun deleteUserAccount(userId: String): Response<ResponseBody>
    suspend fun getUser(token: String): UserInfo?
    suspend fun loginWithDiscord(): Response<Unit>
    suspend fun logout()
    suspend fun refreshCurrentSession()
}
class SupabaseAuthServiceImpl @Inject constructor(
    private val goTrue: GoTrue,
    private val functions: Functions
) : SupabaseAuthService {
    override suspend fun loginWithDiscord(): Response<Unit> {
        return try {
            Response.success(goTrue.loginWith(Discord))
        } catch (e: RestException) {
            Response.error(400, "Bad Request".toResponseBody(null))
        } catch (ioException: HttpRequestTimeoutException) {
            Response.error(408, "Request Timeout".toResponseBody(null))
        } catch (e: HttpRequestException) {
            Response.error(500, "Bad Request".toResponseBody(null))
        }
    }

    override suspend fun getUser(token: String): UserInfo {
        return goTrue.retrieveUser(token)
    }

    override suspend fun refreshCurrentSession() {
        goTrue.refreshCurrentSession()
    }

    override suspend fun currentSession(): UserSession? =
        goTrue.currentSessionOrNull()

    override suspend fun currentAccessToken(): String? =
        goTrue.currentAccessTokenOrNull()

    override suspend fun logout() {
        goTrue.logout()
    }


    override suspend fun deleteUserAccount(userId: String): Response<ResponseBody> =
        try {
            val response = functions.invoke(
                function = "delete-user",
                body = buildJsonObject {
                    put("userId", userId)
                },
                headers = Headers.build {
                    append(HttpHeaders.ContentType, "application/json")
                    append(HttpHeaders.Authorization, "Bearer ${BuildConfig.SUPABASE_API_KEY}")
                }
            )
            if(response.status.value in 200..299) {
                Response.success(response.status.value, "OK".toResponseBody(null))
            } else {
                Response.error(
                    response.status.value,
                    response.status.description.toResponseBody(null)
                )
            }
        } catch (e: RestException) {
            Response.error(400, "Bad Request".toResponseBody(null))
        } catch (ioException: HttpRequestTimeoutException) {
            Response.error(408, "Request Timeout".toResponseBody(null))
        } catch (e: HttpRequestException) {
            Response.error(500, "Bad Request".toResponseBody(null))
        }

}

