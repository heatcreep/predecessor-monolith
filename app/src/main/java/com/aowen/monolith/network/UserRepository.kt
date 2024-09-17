package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.UserInfo
import kotlinx.coroutines.delay
import javax.inject.Inject

const val TABLE_PROFILES = "profiles"

data class ClaimedPlayer(
    val playerStats: PlayerStats? = null,
    val playerDetails: PlayerDetails? = null
)

interface UserRepository {

    suspend fun getUser(): UserInfo?

    suspend fun logout()
}

class UserRepositoryImpl @Inject constructor(
    private val authService: SupabaseAuthService,
    private val postgrestService: SupabasePostgrestService,
) : UserRepository {

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

    override suspend fun logout() {
        authService.signOut()
    }
}
