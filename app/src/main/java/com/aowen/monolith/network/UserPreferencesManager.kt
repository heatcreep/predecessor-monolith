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
    suspend fun saveConsole(console: Console)
}

class UserPreferencesManagerImpl(private val context: Context) : UserPreferencesManager {

    companion object {
        private val CONSOLE_KEY = stringPreferencesKey("console")
    }

    override val console: Flow<Console> = context.userPreferencesDataStore.data
        .map { prefs ->
            prefs[CONSOLE_KEY]?.let { Console.valueOf(it) } ?: Console.PC
        }

    override suspend fun saveConsole(console: Console) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[CONSOLE_KEY] = console.name
        }
    }
}