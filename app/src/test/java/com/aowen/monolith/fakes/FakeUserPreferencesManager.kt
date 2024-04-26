package com.aowen.monolith.fakes

import com.aowen.monolith.data.Console
import com.aowen.monolith.network.UserPreferencesManager
import kotlinx.coroutines.flow.flowOf

class FakeUserPreferencesManager : UserPreferencesManager {
    override val console = flowOf(Console.PC)
    override suspend fun saveConsole(console: Console) {
        // no-op
    }
}