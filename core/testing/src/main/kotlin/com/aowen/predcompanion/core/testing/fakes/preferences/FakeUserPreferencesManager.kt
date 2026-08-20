package com.aowen.predcompanion.core.testing.fakes.preferences

import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.ui.theme.Console
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

abstract class Scenario {
    object SkippedOnboarding : Scenario()
}

class FakeUserPreferencesManager(
    scenario: Scenario? = null,
) : UserPreferencesManager {

    private val _console = MutableStateFlow(Console.PC)
    override val console: Flow<Console> = _console

    override val hasSkippedOnboarding: Flow<Boolean> = when (scenario) {
        Scenario.SkippedOnboarding -> flowOf(true)
        else -> flowOf(false)
    }
    override suspend fun saveConsole(console: Console) {
        _console.value = console
    }

    override suspend fun setSkippedOnboarding() {}
}
