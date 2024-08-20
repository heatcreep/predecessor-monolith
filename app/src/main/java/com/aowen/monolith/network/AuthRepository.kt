package com.aowen.monolith.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class UserProfile(
    @SerialName("player_id")
    val playerId: String? = null
)

interface AuthRepository {

    val accessTokenFlow: Flow<String?>

    suspend fun saveAccessTokenOnSuccessfulLogin()

    suspend fun signInWithDiscord(): Result<Unit?>

    suspend fun getPlayer(): Result<UserProfile?>
    suspend fun handleSavePlayer(playerId: String): Result<Unit>

    suspend fun deleteUserAccount(userId: String): Result<String>
    suspend fun refreshCurrentSessionOnLogin()
}


class AuthRepositoryImpl @Inject constructor(
    private val authService: SupabaseAuthService,
    private val postgrestService: SupabasePostgrestService,
    private val userPreferencesManager: UserPreferencesManager
) : AuthRepository {

    override val accessTokenFlow: Flow<String?> = userPreferencesManager.accessToken

    override suspend fun saveAccessTokenOnSuccessfulLogin() {
        val accessToken = authService.currentAccessToken()
        accessToken?.let {
            userPreferencesManager.saveAccessToken(it)
        }
    }

    override suspend fun refreshCurrentSessionOnLogin() {
        authService.refreshCurrentSession()
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
        return try {
            var session = authService.currentSession()
            var retryCount = 3
            while (session == null && retryCount > 0) {
                delay(500)
                session = authService.currentSession()
                retryCount--
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
        return if(response.isSuccessful) {
            Result.success("Your account has been deleted.")
        } else {
            Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
        }
    }
}