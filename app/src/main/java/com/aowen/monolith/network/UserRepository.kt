package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.UserInfo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

const val TABLE_PROFILES = "profiles"

data class ClaimedUser(
    val playerStats: PlayerStats? = null,
    val playerDetails: PlayerDetails? = null
)

interface UserRepository {
    suspend fun getUser(): UserInfo?

    suspend fun getClaimedUser(): Result<ClaimedUser?>

    fun setClaimedUser(playerStats: PlayerStats?, playerDetails: PlayerDetails?)

    suspend fun logout()
}

class UserRepositoryImpl @Inject constructor(
    private val authService: SupabaseAuthService,
    private val postgrestService: SupabasePostgrestService,
    private val omedaCityRepository: OmedaCityRepository,

) : UserRepository {

    private val _claimedPlayer: MutableStateFlow<ClaimedUser?> = MutableStateFlow(null)
    val claimedPlayer: StateFlow<ClaimedUser?> = _claimedPlayer
    override suspend fun getUser(): UserInfo? {
        var session = authService.currentSession()
        var retryCount = 3
        while (session == null && retryCount > 0) {
            delay(500)
            session = authService.currentSession()
            retryCount--
        }
        val user = session?.let {
            if (it.user?.id != null) {
                postgrestService.fetchUserInfo(it.user?.email!!)
            } else null
        }

        return if (user != null) {
            UserInfo(
                id = user.id,
                updatedAt = user.updatedAt,
                email = user.email,
                fullName = user.fullName,
                avatarUrl = user.avatarUrl,
                playerId = user.playerId
            )
        } else null
    }

    override suspend fun getClaimedUser(): Result<ClaimedUser?> {
        val claimedPlayerInfo = claimedPlayer.value
        return if (claimedPlayerInfo != null) {
            Result.success(claimedPlayerInfo)
        } else {

            var session = authService.currentSession()
            var retryCount = 3
            while (session == null && retryCount > 0) {
                delay(500)
                session = authService.currentSession()
                retryCount--
            }
            val userProfile = session?.let {
                if (it.user?.id != null) {
                    postgrestService.fetchPlayer(it.user?.id!!)
                } else null
            }

            if (userProfile?.playerId != null) {
                coroutineScope {
                    val playerInfoResponse =
                        omedaCityRepository.fetchPlayerInfo(userProfile.playerId)
                    if (playerInfoResponse.isSuccess) {
                        return@coroutineScope Result.success(
                            ClaimedUser(
                                playerStats = playerInfoResponse.getOrNull()?.playerStats,
                                playerDetails = playerInfoResponse.getOrNull()?.playerDetails
                            )
                        )
                    } else {
                        return@coroutineScope Result.failure(Exception("Failed to fetch player info."))
                    }

                }
            } else {
                return Result.failure(Exception("No Player ID found."))
            }
        }
    }

    override fun setClaimedUser(playerStats: PlayerStats?, playerDetails: PlayerDetails?) {
        _claimedPlayer.value = ClaimedUser(
            playerStats = playerStats,
            playerDetails = playerDetails
        )
    }

    override suspend fun logout() {
        authService.logout()
    }
}
