package com.aowen.monolith.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "claimed_player_preferences")

interface ClaimedPlayerPreferencesManager {
    val claimedPlayerId: Flow<String?>
    suspend fun saveClaimedPlayerId(claimedPlayerId: String)
}

class ClaimedPlayerPreferencesManagerImpl(private val context: Context): ClaimedPlayerPreferencesManager {

    companion object {
        val ClAIMED_PLAYER_ID_KEY = stringPreferencesKey("claimed_player_id")
        private val json = Json { ignoreUnknownKeys = true }
    }

    override val claimedPlayerId: Flow<String?> = context.dataStore.data
        .map { prefs ->
            prefs[ClAIMED_PLAYER_ID_KEY]
        }

    override suspend fun saveClaimedPlayerId(claimedPlayerId: String) {
        context.dataStore.edit { prefs ->
            prefs[ClAIMED_PLAYER_ID_KEY] = claimedPlayerId
        }
    }
}