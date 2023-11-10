package com.aowen.monolith.network

import com.aowen.monolith.logDebug
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject

interface SupabasePostgrestService {

    suspend fun fetchPlayer(userId: String): UserProfile?

    suspend fun savePlayer(playerId: String, userId: String)
}

class SupabasePostgrestServiceImpl @Inject constructor(
    private val postgrest: Postgrest
) : SupabasePostgrestService {

    override suspend fun fetchPlayer(userId: String): UserProfile? {
        return postgrest.from(TABLE_PROFILES)
            .select(columns = Columns.raw("player_id")) {
                eq("id", userId)
            }.decodeList<UserProfile>().firstOrNull()
    }

    override suspend fun savePlayer(playerId: String, userId: String) {
        try {
            postgrest[TABLE_PROFILES].update({
                set("player_id", playerId)
            }) {
                eq("id", userId)
            }
        } catch (e: RestException) {
            logDebug(e.localizedMessage)
        }
    }
}