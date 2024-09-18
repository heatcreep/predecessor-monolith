package com.aowen.monolith.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.themePreferencesDataStore by preferencesDataStore(name = "theme_preferences")

enum class Theme {
    SYSTEM,
    LIGHT,
    DARK
}

interface ThemePreferences {
    val theme: Flow<Theme>
    suspend fun saveTheme(theme: Theme)
}

class ThemePreferencesImpl(private val context: Context) : ThemePreferences {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
    }

    override val theme: Flow<Theme> = context.themePreferencesDataStore.data
        .map { prefs ->
            prefs[THEME_KEY]?.let { Theme.valueOf(it) } ?: Theme.SYSTEM
        }

    override suspend fun saveTheme(theme: Theme) {
        context.themePreferencesDataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.name
        }
    }
}