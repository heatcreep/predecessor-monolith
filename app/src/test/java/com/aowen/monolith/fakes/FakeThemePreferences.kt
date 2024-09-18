package com.aowen.monolith.fakes

import com.aowen.monolith.data.datastore.Theme
import com.aowen.monolith.data.datastore.ThemePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeThemePreferences : ThemePreferences {

    override val theme: Flow<Theme> = flowOf(Theme.LIGHT)

    override suspend fun saveTheme(theme: Theme) {
        TODO("Not yet implemented")
    }
}