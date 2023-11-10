package com.aowen.monolith.fakes

import com.aowen.monolith.network.SupabasePostgrestService
import com.aowen.monolith.network.UserProfile

class FakeSupabasePostgrestService: SupabasePostgrestService {
    override suspend fun fetchPlayer(userId: String): UserProfile {
        return UserProfile("fake-player-id")
    }

    override suspend fun savePlayer(playerId: String, userId: String) {
        // no-op
    }
}