package com.aowen.monolith.network

import com.aowen.monolith.data.UserInfo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

const val TABLE_PROFILES = "profiles"

interface UserRepository {
    suspend fun getUser(): UserInfo
}

class UserRepositoryImpl @Inject constructor(
    private val client: SupabaseClient
) : UserRepository {
    override suspend fun getUser(): UserInfo {
        val user = client.postgrest[TABLE_PROFILES].select().decodeSingle<UserInfo>()
        return UserInfo(
            id = user.id,
            updatedAt = user.updatedAt,
            email = user.email,
            fullName = user.fullName,
            avatarUrl = user.avatarUrl
        )
    }
}
