package com.aowen.monolith.network

import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.providers.Discord
import io.github.jan.supabase.gotrue.user.UserSession
import io.ktor.client.plugins.HttpRequestTimeoutException
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

interface SupabaseAuthService {
    suspend fun loginWithDiscord(): Response<Unit>

    suspend fun currentSession(): UserSession?
}

class SupabaseAuthServiceImpl @Inject constructor(
    private val goTrue: GoTrue
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


    override suspend fun currentSession(): UserSession? =
        goTrue.currentSessionOrNull()

}