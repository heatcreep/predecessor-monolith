package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import com.aowen.monolith.data.UserInfo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

const val TABLE_PROFILES = "profiles"

data class ClaimedUser(
    val playerStats: PlayerStats? = null,
    val playerDetails: PlayerDetails? = null
)

interface UserRepository {
    suspend fun getUser(): UserInfo?

    suspend fun getClaimedUser(): Result<ClaimedUser?>

    suspend fun setClaimedUser(playerStats: PlayerStats?, playerDetails: PlayerDetails?)

    suspend fun logout()
}

class UserRepositoryImpl @Inject constructor(
    private val client: SupabaseClient,
    private val omedaCityRepository: OmedaCityRepository

) : UserRepository {
    override suspend fun getUser(): UserInfo? {
        val user = try {
            var session = client.gotrue.currentSessionOrNull()
            var retryCount = 3
            while (session == null && retryCount > 0) {
                delay(500)
                session = client.gotrue.currentSessionOrNull()
                retryCount--
            }
            session?.let {
                if (it.user?.id != null) {
                    client.postgrest.from(TABLE_PROFILES)
                        .select(
                            columns = Columns.list(
                                "email",
                                "full_name",
                                "avatar_url",
                                "id",
                                "updated_at"
                            )
                        ) {
                            eq("email", it.user?.email!!)
                        }.decodeList<UserInfo>().firstOrNull()
                } else null
            }
        } catch (e: Exception) {
            null
        }
        return if (user != null) {
            UserInfo(
                id = user.id,
                updatedAt = user.updatedAt,
                email = user.email,
                fullName = user.fullName,
                avatarUrl = user.avatarUrl
            )
        } else null
    }

    private val claimedPlayer = MutableStateFlow<ClaimedUser?>(null)

    override suspend fun getClaimedUser(): Result<ClaimedUser?> {
        val claimedPlayerInfo = claimedPlayer.value
        return if (claimedPlayerInfo != null) {
            Result.success(claimedPlayerInfo)
        } else {
            val userProfile = try {
                var session = client.gotrue.currentSessionOrNull()
                var retryCount = 3
                while (session == null && retryCount > 0) {
                    delay(500)
                    session = client.gotrue.currentSessionOrNull()
                    retryCount--
                }
                session?.let {
                    if (it.user?.id != null) {
                        client.postgrest.from(TABLE_PROFILES)
                            .select(columns = Columns.raw("player_id")) {
                                eq("id", it.user?.id!!)
                            }.decodeList<UserProfile>().firstOrNull()
                    } else null
                }
            } catch (e: Exception) {
                null
            }
            if (userProfile?.playerId != null) {
                coroutineScope {
                    val playerInfoResponse = omedaCityRepository.fetchPlayerInfo(userProfile.playerId)
                    if(playerInfoResponse.isSuccess) {
                        return@coroutineScope Result.success(ClaimedUser(
                            playerStats = playerInfoResponse.getOrNull()?.playerStats,
                            playerDetails = playerInfoResponse.getOrNull()?.playerDetails
                        ))
                    } else {
                        return@coroutineScope Result.failure(Exception("Failed to fetch player info."))
                    }

                }
            } else {
                return Result.failure(Exception("No Player ID found."))
            }
        }
    }

    override suspend fun setClaimedUser(playerStats: PlayerStats?, playerDetails: PlayerDetails?) {
        claimedPlayer.update {
            it?.copy(
                playerStats = playerStats,
                playerDetails = playerDetails
            )
        }
    }

    override suspend fun logout() {
        client.gotrue.logout()
    }
}
