package com.aowen.predcompanion.core.data.repository.auth

import com.aowen.predcompanion.core.database.dao.ClaimedPlayerDao
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.network.SupabaseAuthService
import com.aowen.predcompanion.core.network.SupabasePostgrestService
import com.aowen.predcompanion.core.network.model.NetworkUserProfile
import com.aowen.predcompanion.core.network.model.NetworkUserState
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRepository {

    val networkUserState: StateFlow<NetworkUserState>

    suspend fun handleSuccessfulLoginFromDiscord()

    suspend fun signInWithDiscord(): Result<Unit?>

    suspend fun getPlayer(): Result<NetworkUserProfile?>
    suspend fun handleSavePlayer(playerId: String): Result<Unit>

    suspend fun deleteUserAccount(userId: String): Result<String>
    suspend fun getCurrentSessionStatus()
}


@Singleton
class SupabaseAuthRepository @Inject constructor(
    private val authService: SupabaseAuthService,
    private val claimedPlayerDao: ClaimedPlayerDao,
    private val postgrestService: SupabasePostgrestService,
    private val userPreferencesManager: UserPreferencesManager
) : AuthRepository {

    private val _Network_userState = MutableStateFlow<NetworkUserState>(NetworkUserState.Loading)
    override val networkUserState: StateFlow<NetworkUserState> = _Network_userState

    override suspend fun handleSuccessfulLoginFromDiscord() {
        _Network_userState.update { NetworkUserState.Authenticated }
    }

    override suspend fun getCurrentSessionStatus() {
        val hasSkippedOnboarding = userPreferencesManager.hasSkippedOnboarding.first()
        try {
            val sessionStatus = authService.awaitAuthService().first()
            when (sessionStatus) {
                is SessionStatus.LoadingFromStorage -> _Network_userState.update { NetworkUserState.Loading }
                is SessionStatus.Authenticated -> _Network_userState.update { NetworkUserState.Authenticated }
                is SessionStatus.NotAuthenticated -> {
                    _Network_userState.update {
                        NetworkUserState.Unauthenticated(hasSkippedOnboarding = hasSkippedOnboarding)
                    }
                }

                else -> {
                    throw Exception("Network error")
                }
            }

        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun signInWithDiscord(): Result<Unit?> {

        val response = authService.loginWithDiscord()
        return if (response.isSuccessful) {
            Result.success(response.body())
        } else {
            Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
        }
    }

    override suspend fun getPlayer(): Result<NetworkUserProfile?> {
        when (networkUserState.value) {
            is NetworkUserState.Unauthenticated -> {
                val playerId = claimedPlayerDao.getClaimedPlayerIds().firstOrNull()?.firstOrNull()
                return Result.success(NetworkUserProfile(playerId))
            }

            is NetworkUserState.Authenticated -> {
                return try {
                    val session = withTimeoutOrNull(1000) {
                        authService.currentSession()
                    }
                    session?.let {
                        if (it.user?.id != null) {
                            Result.success(
                                postgrestService.fetchPlayer(it.user?.id!!)
                            )
                        } else Result.failure(Exception("No user id"))
                    } ?: Result.failure(Exception("No current session"))
                } catch (e: Exception) {
                    return Result.failure(e)
                }
            }

            else -> return Result.success(null)
        }
    }

    override suspend fun handleSavePlayer(playerId: String): Result<Unit> {
        return try {
            authService.currentSession()?.let {
                if (it.user?.id != null) {
                    Result.success(postgrestService.savePlayer(playerId, it.user?.id!!))
                } else Result.failure(Exception("No user id"))
            } ?: Result.failure(Exception("No current session"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun deleteUserAccount(userId: String): Result<String> {
        val response = authService.deleteUserAccount(userId = userId)
        return if (response.isSuccessful) {
            Result.success("Your account has been deleted.")
        } else {
            Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
        }
    }
}