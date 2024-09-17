package com.aowen.monolith.network

import com.aowen.monolith.data.database.dao.ClaimedPlayerDao
import com.aowen.monolith.logDebug
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class UserProfile(
    @SerialName("player_id")
    val playerId: String? = null
)

abstract class UserState {
    object Loading : UserState()
    data class Unauthenticated(val hasSkippedOnboarding: Boolean) : UserState()
    object Authenticated : UserState()
}

interface AuthRepository {

    val userState: StateFlow<UserState>

    suspend fun handleSuccessfulLoginFromDiscord()

    suspend fun signInWithDiscord(): Result<Unit?>

    suspend fun getPlayer(): Result<UserProfile?>
    suspend fun handleSavePlayer(playerId: String): Result<Unit>

    suspend fun deleteUserAccount(userId: String): Result<String>
    suspend fun getCurrentSessionStatus()
}


class AuthRepositoryImpl @Inject constructor(
    private val authService: SupabaseAuthService,
    private val claimedPlayerDao: ClaimedPlayerDao,
    private val postgrestService: SupabasePostgrestService,
    private val userPreferencesManager: UserPreferencesManager
) : AuthRepository {

    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    override val userState: StateFlow<UserState> = _userState

    override suspend fun handleSuccessfulLoginFromDiscord() {
        _userState.update { UserState.Authenticated }
    }

    override suspend fun getCurrentSessionStatus() {
        val hasSkippedOnboarding = userPreferencesManager.hasSkippedOnboarding.first()
        try {
            val sessionStatus = authService.awaitAuthService().first()
            logDebug("Session status: $sessionStatus", "AuthViewModel")
            when (sessionStatus) {
                is SessionStatus.LoadingFromStorage -> _userState.update { UserState.Loading }
                is SessionStatus.Authenticated -> _userState.update { UserState.Authenticated }
                is SessionStatus.NotAuthenticated -> {
                    _userState.update {
                        UserState.Unauthenticated(hasSkippedOnboarding = hasSkippedOnboarding)
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

    override suspend fun getPlayer(): Result<UserProfile?> {
        when (userState.value) {
            is UserState.Unauthenticated -> {
                val playerId = claimedPlayerDao.getClaimedPlayerIds().firstOrNull()?.firstOrNull()
                return Result.success(UserProfile(playerId))
            }

            is UserState.Authenticated -> {
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