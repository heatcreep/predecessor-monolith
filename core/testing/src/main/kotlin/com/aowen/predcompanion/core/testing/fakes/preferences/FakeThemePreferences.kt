package com.aowen.predcompanion.core.testing.fakes.preferences

import com.aowen.predcompanion.core.datastore.Theme
import com.aowen.predcompanion.core.datastore.ThemePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeThemePreferences : ThemePreferences {

    private val _theme = MutableStateFlow(Theme.SYSTEM)
    override val theme: StateFlow<Theme> = _theme

    override suspend fun saveTheme(theme: Theme) {
        _theme.value = theme
    }
}
