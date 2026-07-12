package com.aowen.predcompanion.core.data.repository.auth

import android.content.Intent
import androidx.core.net.toUri
import com.aowen.predcompanion.core.datastore.AuthStateDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject
import kotlin.coroutines.resume

interface NewAuthRepository {

    val isLoggedIn: Flow<Boolean>

    fun loginIntent(): Intent

    suspend fun onLoginResult(intent: Intent): Result<Unit>

    suspend fun freshAccessToken(): String?
}

class AppAuthRepository @Inject constructor(
    private val authService: AuthorizationService,
    private val store: AuthStateDataStore,
) : NewAuthRepository {

    private val config = AuthorizationServiceConfiguration(
        AUTHORIZATION_URL.toUri(),
        TOKEN_URL.toUri()
    )

    private val refreshMutex = Mutex()

    override val isLoggedIn: Flow<Boolean> = store.authStateFlow.map { it?.isAuthorized == true }

    override fun loginIntent(): Intent {
        val request = AuthorizationRequest
            .Builder(
                config,
                CLIENT_ID,
                ResponseTypeValues.CODE,
                REDIRECT_URI.toUri()
            )
            .setScope("guide:read guide:read:private offline_access profile guide:write")
            .build()
        return authService.getAuthorizationRequestIntent(request)
    }

    override suspend fun onLoginResult(intent: Intent): Result<Unit> {
        val res = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        if (res == null) return Result.failure(ex ?: IllegalStateException("No auth response"))

        val state = AuthState(res, ex)
        return suspendCancellableCoroutine { cont ->
            authService.performTokenRequest(res.createTokenExchangeRequest()) { tokenResponse, tokenException ->
                state.update(tokenResponse, tokenException)
                cont.resume(
                    if (tokenResponse != null) Result.success(Unit)
                    else Result.failure(
                        tokenException ?: IllegalStateException("No token response")
                    )
                )
            }
        }.also { if (it.isSuccess) store.write(state) }
    }

    override suspend fun freshAccessToken(): String? = refreshMutex.withLock {
        val state = store.read() ?: return null
        val token = suspendCancellableCoroutine { cont ->
            state.performActionWithFreshTokens(authService) { accessToken, _, ex ->
                cont.resume(if (ex == null) accessToken else null)
            }
        }
        if (token != null) store.write(state) else store.clear() // persist rotation, or drop a dead session
        token
    }

    companion object {
        const val AUTHORIZATION_URL = "https://pred.gg/api/oauth2/authorize"
        const val TOKEN_URL = "https://pred.gg/api/oauth2/token"
        const val CLIENT_ID = "9uizycicgvr5mbkmujpodhpni5bcoqyk"
        const val REDIRECT_URI = "com.aowen.predcompanion://login"
    }

}