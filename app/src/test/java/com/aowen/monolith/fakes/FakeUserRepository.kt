package com.aowen.monolith.fakes

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.network.ClaimedUser
import com.aowen.monolith.network.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class FakeUserRepository(
    private val error: Boolean = false,
    fakeClaimedUser: ClaimedUser? = null
) : UserRepository {

    private val _claimedUser: MutableStateFlow<ClaimedUser?> = MutableStateFlow(fakeClaimedUser)
    val claimedUser: StateFlow<ClaimedUser?> = _claimedUser

    private val _logoutCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val logoutCounter: StateFlow<Int> = _logoutCounter

    override suspend fun getUser(): UserInfo? {
        return if (error) {
            null
        } else UserInfo(UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57511"))
    }

    override suspend fun getClaimedUser(): Result<ClaimedUser?> {
        return if (error) {
            Result.failure(Exception("Error getting claimed user."))
        } else {
            when (_claimedUser.value) {
                null -> Result.success(null)
                else -> Result.success(claimedUser.value)
            }
        }
    }

    override fun setClaimedUser(playerStats: PlayerStats?, playerDetails: PlayerDetails?) {
        _claimedUser.update { ClaimedUser(playerStats, playerDetails) }
    }

    override suspend fun logout() {
        _logoutCounter.update { it + 1 }
    }
}