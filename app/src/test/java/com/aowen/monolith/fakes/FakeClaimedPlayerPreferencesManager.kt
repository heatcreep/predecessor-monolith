package com.aowen.monolith.fakes

import com.aowen.monolith.network.ClaimedPlayerPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeClaimedPlayerPreferencesManager(): ClaimedPlayerPreferencesManager {

    override val claimedPlayerId: Flow<String?> = flowOf("testPlayerId")

    override suspend fun saveClaimedPlayerId(claimedPlayerId: String) {
        // no-op
    }
}