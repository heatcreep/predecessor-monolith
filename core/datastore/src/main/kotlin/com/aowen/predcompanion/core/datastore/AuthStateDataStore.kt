package com.aowen.predcompanion.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.openid.appauth.AuthState
import javax.inject.Inject

private val Context.authDataStore by preferencesDataStore("auth")

class AuthStateDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val key = stringPreferencesKey("auth_state")

    val authStateFlow: Flow<AuthState?> =
        context.authDataStore.data.map { it[key]?.let(AuthState::jsonDeserialize) }

    suspend fun read(): AuthState? =
        context.authDataStore.data.first()[key]?.let(AuthState::jsonDeserialize)

    suspend fun write(state: AuthState) =
        context.authDataStore.edit { it[key] = state.jsonSerializeString() }.let {}

    suspend fun clear() = context.authDataStore.edit { it.remove(key) }.let {}
}