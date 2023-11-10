package com.aowen.monolith.fakes

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.network.ClaimedUser
import com.aowen.monolith.network.UserRepository
import java.util.UUID

class FakeUserRepository(private val error: Boolean = false) : UserRepository {
    override suspend fun getUser(): UserInfo? {
        return if (error) {
            null
        } else UserInfo(UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57511"))
    }

    override suspend fun getClaimedUser(): Result<ClaimedUser?> {
        TODO("Not yet implemented")
    }

    override fun setClaimedUser(playerStats: PlayerStats?, playerDetails: PlayerDetails?) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }
}