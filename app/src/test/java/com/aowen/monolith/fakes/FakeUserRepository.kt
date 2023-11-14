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

class FakeUserRepository(private val error: Boolean = false) : UserRepository {

    private val _claimedPlayer: MutableStateFlow<ClaimedUser?> = MutableStateFlow(null)
    val claimedPlayer: StateFlow<ClaimedUser?> = _claimedPlayer

    private val _logoutCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val logoutCounter: StateFlow<Int> = _logoutCounter

    override suspend fun getUser(): UserInfo? {
        return if (error) {
            null
        } else UserInfo(UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57511"))
    }

    override suspend fun getClaimedUser(): Result<ClaimedUser?> {
        TODO("Not yet implemented")
    }

    override fun setClaimedUser(playerStats: PlayerStats?, playerDetails: PlayerDetails?) {
        _claimedPlayer.update { ClaimedUser(playerStats, playerDetails) }
    }

    override suspend fun logout() {
        _logoutCounter.update { it + 1 }
    }
}