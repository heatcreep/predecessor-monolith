package com.aowen.monolith.fakes

import com.aowen.monolith.data.Console
import com.aowen.monolith.network.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

abstract class Scenario {
    object SkippedOnboarding : Scenario()
}

class FakeUserPreferencesManager(
    scenario: Scenario? = null,

    ) : UserPreferencesManager {
    override val console = flowOf(Console.PC)
    override val hasSkippedOnboarding: Flow<Boolean> = when (scenario) {
        Scenario.SkippedOnboarding -> flowOf(true)
        else -> flowOf(false)
    }

    val _saveConsoleCoutner = MutableStateFlow(0)
    val saveConsoleCounter: StateFlow<Int> = _saveConsoleCoutner

    val _saveAccessTokenCounter = MutableStateFlow(0)
    val saveAccessTokenCounter: StateFlow<Int> = _saveAccessTokenCounter

    val _clearAccessTokenCounter = MutableStateFlow(0)
    val clearAccessTokenCounter: StateFlow<Int> = _clearAccessTokenCounter

    override suspend fun saveConsole(console: Console) {
        _saveConsoleCoutner.value++
    }

    override suspend fun setSkippedOnboarding() {
        TODO("Not yet implemented")
    }
}