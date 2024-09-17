package com.aowen.monolith.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aowen.monolith.data.Console
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")

interface UserPreferencesManager {
    val console: Flow<Console>
    val hasSkippedOnboarding: Flow<Boolean>
    suspend fun saveConsole(console: Console)
    suspend fun setSkippedOnboarding()
}

class UserPreferencesManagerImpl(private val context: Context) : UserPreferencesManager {

    companion object {
        private val CONSOLE_KEY = stringPreferencesKey("console")
        private val SKIPPED_ONBOARDING_KEY = stringPreferencesKey("skipped_onboarding")
        private val SYNCED_SAVED_CONTENT_KEY = stringPreferencesKey("synced_saved_content")
    }

    override val console: Flow<Console> = context.userPreferencesDataStore.data
        .map { prefs ->
            prefs[CONSOLE_KEY]?.let { Console.valueOf(it) } ?: Console.PC
        }

    override val hasSkippedOnboarding: Flow<Boolean> = context.userPreferencesDataStore.data
        .map { prefs ->
            prefs[SKIPPED_ONBOARDING_KEY]?.let { it.toBoolean() } ?: false
        }

    override suspend fun saveConsole(console: Console) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[CONSOLE_KEY] = console.name
        }
    }

    override suspend fun setSkippedOnboarding() {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[SKIPPED_ONBOARDING_KEY] = true.toString()
        }
    }
}