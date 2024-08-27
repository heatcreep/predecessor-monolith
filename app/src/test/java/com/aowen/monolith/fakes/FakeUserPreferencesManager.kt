package com.aowen.monolith.fakes

import com.aowen.monolith.data.Console
import com.aowen.monolith.network.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserPreferencesManager : UserPreferencesManager {
    override val console = flowOf(Console.PC)
    override val accessToken: Flow<String?> = flowOf(null)
    override val hasSkippedOnboarding: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun saveConsole(console: Console) {
        // no-op
    }

    override suspend fun saveAccessToken(accessToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearAccessToken() {
        TODO("Not yet implemented")
    }

    override suspend fun setHasSkippedOnboarding(hasSkippedOnboarding: Boolean) {
        TODO("Not yet implemented")
    }
}