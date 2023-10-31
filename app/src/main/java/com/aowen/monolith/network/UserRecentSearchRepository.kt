package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerSearchDto
import com.aowen.monolith.data.create
import com.aowen.monolith.logDebug
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

const val TABLE_USER_ID = "id"
const val TABLE_PLAYER_ID = "player_id"
const val TABLE_CREATED_AT = "created_at"
const val TABLE_RECENT_PROFILES = "recent_profiles"
const val TABLE_MAX_ROWS = 10

interface UserRecentSearchRepository {

    suspend fun getRecentSearches(): List<PlayerDetails>
    suspend fun addRecentSearch(playerDetails: PlayerDetails): Boolean
    suspend fun removeRecentSearch(playerId: String)

    suspend fun removeAllRecentSearches()
}

class UserRecentSearchRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val userRepository: UserRepository
) : UserRecentSearchRepository {

    override suspend fun getRecentSearches(): List<PlayerDetails> {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                return emptyList()
            } else {
                withContext(Dispatchers.IO) {
                    postgrest[TABLE_RECENT_PROFILES].select {
                        eq(TABLE_USER_ID, user.id)
                        order(TABLE_CREATED_AT, Order.DESCENDING)
                    }.decodeList<PlayerSearchDto>()
                        .map { it.create() }
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addRecentSearch(playerDetails: PlayerDetails): Boolean {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                return false
            } else {
                withContext(Dispatchers.IO) {
                    val playerSearchDto = PlayerSearchDto(
                        createdAt = Timestamp(System.currentTimeMillis()).toString(),
                        id = user.id,
                        playerId = UUID.fromString(playerDetails.playerId),
                        displayName = playerDetails.playerName,
                        isRanked = playerDetails.isRanked,
                        region = playerDetails.region,
                        rank = playerDetails.rank,
                        rankTitle = playerDetails.rankTitle,
                        rankImage = playerDetails.rankImage,
                        mmr = playerDetails.mmr?.toFloat(),
                    )
                    val recentSearches =
                        postgrest[TABLE_RECENT_PROFILES].select {
                            eq(TABLE_USER_ID, user.id)
                            order(TABLE_CREATED_AT, Order.ASCENDING)
                        }.decodeList<PlayerSearchDto>()

                    if (recentSearches.any { it.playerId == playerSearchDto.playerId }.not()) {
                        if (recentSearches.size >= TABLE_MAX_ROWS) {
                            postgrest[TABLE_RECENT_PROFILES].delete {
                                eq(TABLE_USER_ID, user.id)
                                eq(TABLE_PLAYER_ID, recentSearches.first().playerId)
                            }
                            postgrest[TABLE_RECENT_PROFILES].insert(playerSearchDto)
                        } else {
                            if(playerSearchDto.playerId != UUID.fromString(user.playerId)) {
                                postgrest[TABLE_RECENT_PROFILES].insert(playerSearchDto)
                            }
                        }
                    } else {
                        postgrest[TABLE_RECENT_PROFILES].update(playerSearchDto) {
                            eq(TABLE_USER_ID, user.id)
                            eq(TABLE_PLAYER_ID, playerSearchDto.playerId)
                        }
                    }
                    true
                }
            }

            true
        } catch (e: Exception) {
            logDebug(e.toString())
            false
        }
    }

    override suspend fun removeRecentSearch(playerId: String) {
        val user = userRepository.getUser()
        try {
            if (user?.id == null) {
                return
            } else {
                withContext(Dispatchers.IO) {
                    postgrest[TABLE_RECENT_PROFILES].delete {
                        eq(TABLE_USER_ID, user.id)
                        eq(TABLE_PLAYER_ID, UUID.fromString(playerId))
                    }
                }
            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }

    override suspend fun removeAllRecentSearches() {
        val user = userRepository.getUser()
        try {
            if (user?.id == null) {
                return
            } else {
                withContext(Dispatchers.IO) {
                    postgrest[TABLE_RECENT_PROFILES].delete {
                        eq(TABLE_USER_ID, user.id)
                    }
                }
            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }
}