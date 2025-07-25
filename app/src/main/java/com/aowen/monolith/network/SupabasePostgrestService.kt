package com.aowen.monolith.network

import com.aowen.monolith.data.FavoriteBuildDto
import com.aowen.monolith.data.PlayerSearchDto
import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.logDebug
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

const val COLUMN_RANK = "rank"
const val COLUMN_RANK_TITLE = "rank_title"
const val COLUMN_RANK_IMAGE = "rank_image"
const val COLUMN_IS_RANKED = "is_ranked"
const val COLUMN_MMR = "mmr"


interface SupabasePostgrestService {

    suspend fun fetchPlayer(userId: String): UserProfile?

    /**
     * Links a player to a user in supabase as their claimed player
     */
    suspend fun savePlayer(playerId: String, userId: String)

    suspend fun fetchUserInfo(email: String): UserInfo?

    suspend fun fetchRecentSearches(id: UUID): List<PlayerSearchDto>

    suspend fun deleteAllRecentSearches(userId: UUID)

    suspend fun deleteRecentSearch(userId: UUID, recentPlayerId: UUID)

    suspend fun insertRecentSearch(playerSearchDto: PlayerSearchDto)

    suspend fun updateRecentSearch(
        userId: UUID,
        recentPlayerId: UUID,
        playerSearchDto: PlayerSearchDto
    )

    suspend fun fetchFavoriteBuilds(userId: UUID): List<FavoriteBuildDto>

    suspend fun insertFavoriteBuild(favoriteBuildDto: FavoriteBuildDto)

    suspend fun deleteFavoriteBuild(userId: UUID, buildId: Int)

    suspend fun deleteAllFavoriteBuilds(userId: UUID)

//    suspend fun fetchAllUserBuilds(): Result<List<BuildListItem>?>
}

class SupabasePostgrestServiceImpl @Inject constructor(
    private val postgrest: Postgrest
) : SupabasePostgrestService {

    override suspend fun fetchPlayer(userId: String): UserProfile? {
        return try {
            postgrest.from(TABLE_PROFILES)
                .select(columns = Columns.raw("player_id")) {
                    filter {
                        eq("id", userId)
                    }
                }.decodeList<UserProfile>().firstOrNull()
        } catch (e: HttpRequestTimeoutException) {
            logDebug("$e : the error")
            UserProfile()
        }
    }

    override suspend fun savePlayer(playerId: String, userId: String) {
        try {
            postgrest[TABLE_PROFILES].update({
                set("player_id", playerId)
            }) {
                filter {
                    eq("id", userId)
                }
            }
        } catch (e: RestException) {
            logDebug(e.localizedMessage ?: "Error saving player")
        }
    }

    override suspend fun fetchUserInfo(email: String): UserInfo? {
        return try {
            postgrest.from(TABLE_PROFILES)
                .select(
                    columns = Columns.list(
                        "email",
                        "full_name",
                        "avatar_url",
                        "id",
                        "updated_at",
                        "player_id",
                        "onboarded"
                    )
                ) {
                    filter {
                        eq("email", email)
                    }
                }.decodeList<UserInfo>().firstOrNull()
        } catch (e: HttpRequestTimeoutException) {
            logDebug("$e : the error2")
            UserInfo()
        }
    }

    override suspend fun fetchRecentSearches(id: UUID): List<PlayerSearchDto> {

        return postgrest[TABLE_RECENT_PROFILES].select {
            filter {
                eq(TABLE_ID, id)
            }
            order(TABLE_CREATED_AT, Order.DESCENDING)
        }.decodeList()
    }

    override suspend fun deleteAllRecentSearches(userId: UUID) {
        try {
            postgrest[TABLE_RECENT_PROFILES].delete {
                filter {
                    eq(TABLE_ID, userId)
                }
            }
        } catch (e: Exception) {
            logDebug(e.localizedMessage ?: "Error deleting all recent searches")
        }
    }

    override suspend fun deleteRecentSearch(userId: UUID, recentPlayerId: UUID) {
        postgrest[TABLE_RECENT_PROFILES].delete {
            filter {
                eq(TABLE_ID, userId)
                eq(TABLE_PLAYER_ID, recentPlayerId)
            }
        }
    }

    override suspend fun insertRecentSearch(playerSearchDto: PlayerSearchDto) {
        try {
            postgrest[TABLE_RECENT_PROFILES].insert(playerSearchDto)
        } catch (e: RestException) {
            logDebug("Error inserting recent search: ${e.localizedMessage ?: "Unknown error"}")
        } catch (e: HttpRequestTimeoutException) {
            logDebug("Request timed out while inserting recent search: ${e.localizedMessage ?: "Unknown error"}")
        } catch (e: HttpRequestException) {
            logDebug("HTTP request error while inserting recent search: ${e.localizedMessage ?: "Unknown error"}")
        }
    }

    override suspend fun updateRecentSearch(
        userId: UUID,
        recentPlayerId: UUID,
        playerSearchDto: PlayerSearchDto
    ) {
        postgrest[TABLE_RECENT_PROFILES].update(update = {
            set(TABLE_CREATED_AT, Timestamp(System.currentTimeMillis()).toString())
            set(COLUMN_RANK, playerSearchDto.rank)
            set(COLUMN_MMR, playerSearchDto.mmr)
        }) {
            filter {
                eq(TABLE_ID, userId)
                eq(TABLE_PLAYER_ID, recentPlayerId)
            }
        }
    }

    override suspend fun fetchFavoriteBuilds(userId: UUID): List<FavoriteBuildDto> {
        return postgrest[TABLE_FAVORITE_BUILDS].select {
            filter {
                eq(TABLE_USER_ID, userId)
            }
            order(TABLE_CREATED_AT, Order.DESCENDING)
        }.decodeList()
    }

    override suspend fun insertFavoriteBuild(favoriteBuildDto: FavoriteBuildDto) {
        postgrest[TABLE_FAVORITE_BUILDS].insert(favoriteBuildDto)
    }

    override suspend fun deleteFavoriteBuild(userId: UUID, buildId: Int) {
        postgrest[TABLE_FAVORITE_BUILDS].delete {
            filter {
                eq(TABLE_USER_ID, userId)
                eq(TABLE_BUILD_ID, buildId)
            }
        }
    }

    override suspend fun deleteAllFavoriteBuilds(userId: UUID) {
        try {
            postgrest[TABLE_FAVORITE_BUILDS].delete {
                filter {
                    eq(TABLE_USER_ID, userId)
                }
            }
        } catch (e: Exception) {
            logDebug(e.localizedMessage ?: "Error deleting all favorite builds")
        }
    }

    // TODO: Implement this
//    override suspend fun fetchAllUserBuilds(): Result<List<BuildListItem>?> {
//        return postgrest.from(TABLE_BUILDS)
//            .select(
//                columns = Columns.list(
//                    "id",
//                    "created_at",
//                    "updated_at",
//                    "title",
//                    "author",
//                    "role",
//                    "crest_id",
//                    "items",
//                    "items_first_buy",
//                    "items_flex",
//                    "items_core",
//                    "skill_order",
//                    "description",
//                    "hero_id",
//                )
//            ) {
//                order("created_at", Order.ASCENDING)
//            }.decodeList<BuildDto>().let { buildListDto ->
//                Result.success(buildListDto.map { it.create() })
//            }
//
//    }
}