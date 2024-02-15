package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerSearchDto
import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.logDebug
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
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

    suspend fun savePlayer(playerId: String, userId: String)

    suspend fun fetchUserInfo(email: String): UserInfo?

    suspend fun fetchRecentSearches(id: UUID): List<PlayerSearchDto>

    suspend fun deleteAllRecentSearches(userId: UUID)

    suspend fun deleteRecentSearch(userId: UUID, recentPlayerId: UUID)

    suspend fun insertRecentSearch(playerSearchDto: PlayerSearchDto)

    suspend fun updateRecentSearch(
        userId: UUID,
        recentPlayerId: UUID,
        rankImage: String,
        playerSearchDto: PlayerSearchDto
    )

//    suspend fun fetchAllUserBuilds(): Result<List<BuildListItem>?>
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
            logDebug(e.localizedMessage ?: "Error saving player")
        }
    }

    override suspend fun fetchUserInfo(email: String): UserInfo? {
        return postgrest.from(TABLE_PROFILES)
            .select(
                columns = Columns.list(
                    "email",
                    "full_name",
                    "avatar_url",
                    "id",
                    "updated_at",
                    "player_id"
                )
            ) {
                eq("email", email)
            }.decodeList<UserInfo>().firstOrNull()
    }

    override suspend fun fetchRecentSearches(id: UUID): List<PlayerSearchDto> {

        return postgrest[TABLE_RECENT_PROFILES].select {
            eq(TABLE_USER_ID, id)
            order(TABLE_CREATED_AT, Order.DESCENDING)
        }.decodeList()
    }

    override suspend fun deleteAllRecentSearches(userId: UUID) {
        try {
            postgrest[TABLE_RECENT_PROFILES].delete {
                eq(TABLE_USER_ID, userId)
            }
        } catch (e: Exception) {
            logDebug(e.localizedMessage ?: "Error deleting all recent searches")
        }
    }

    override suspend fun deleteRecentSearch(userId: UUID, recentPlayerId: UUID) {
        postgrest[TABLE_RECENT_PROFILES].delete {
            eq(TABLE_USER_ID, userId)
            eq(TABLE_PLAYER_ID, recentPlayerId)
        }
    }

    override suspend fun insertRecentSearch(playerSearchDto: PlayerSearchDto) {
        postgrest[TABLE_RECENT_PROFILES].insert(playerSearchDto)
    }

    override suspend fun updateRecentSearch(
        userId: UUID,
        recentPlayerId: UUID,
        rankImage: String,
        playerSearchDto: PlayerSearchDto
    ) {
        postgrest[TABLE_RECENT_PROFILES].update(update = {
            set(TABLE_CREATED_AT, Timestamp(System.currentTimeMillis()).toString())
            set(COLUMN_RANK, playerSearchDto.rank)
            set(COLUMN_RANK_TITLE, playerSearchDto.rankTitle)
            set(COLUMN_RANK_IMAGE, rankImage)
            set(COLUMN_IS_RANKED, playerSearchDto.isRanked)
            set(COLUMN_MMR, playerSearchDto.mmr)
        }) {
            eq(TABLE_USER_ID, userId)
            eq(TABLE_PLAYER_ID, recentPlayerId)
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