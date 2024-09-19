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
    val claimedPlayerName: Flow<String?>
    val hasSkippedOnboarding: Flow<Boolean>
    suspend fun saveConsole(console: Console)
    suspend fun saveClaimedPlayerName(playerName: String?)
    suspend fun setSkippedOnboarding()
}

class UserPreferencesManagerImpl(private val context: Context) : UserPreferencesManager {

    companion object {
        private val CONSOLE_KEY = stringPreferencesKey("console")
        private val CLAIMED_PLAYER_NAME_KEY = stringPreferencesKey("claimed_player_name")
        private val SKIPPED_ONBOARDING_KEY = stringPreferencesKey("skipped_onboarding")
    }

    override val console: Flow<Console> = context.userPreferencesDataStore.data
        .map { prefs ->
            prefs[CONSOLE_KEY]?.let { Console.valueOf(it) } ?: Console.PC
        }

    override val claimedPlayerName: Flow<String?> = context.userPreferencesDataStore.data
        .map { prefs ->
            prefs[CLAIMED_PLAYER_NAME_KEY]
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

    override suspend fun saveClaimedPlayerName(playerName: String?) {
        if(playerName == null) {
            context.userPreferencesDataStore.edit { prefs ->
                prefs.remove(CLAIMED_PLAYER_NAME_KEY)
            }
        } else {
            context.userPreferencesDataStore.edit { prefs ->
                prefs[CLAIMED_PLAYER_NAME_KEY] = playerName
            }
        }
    }

    override suspend fun setSkippedOnboarding() {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[SKIPPED_ONBOARDING_KEY] = true.toString()
        }
    }
}