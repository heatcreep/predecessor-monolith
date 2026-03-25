package com.aowen.monolith.network

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerSearchDto
import com.aowen.monolith.data.RankDetails
import com.aowen.monolith.data.create
import com.aowen.monolith.data.repository.players.di.PlayerRepository
import com.aowen.monolith.logDebug
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

interface UserRecentSearchRepository {

    suspend fun getRecentSearches(): List<PlayerDetails>
    suspend fun addRecentSearch(playerDetails: PlayerDetails)
    suspend fun removeRecentSearch(playerId: String)

    suspend fun removeAllRecentSearches()
}

class UserRecentSearchRepositoryImpl @Inject constructor(
    private val postgrestService: SupabasePostgrestService,
    private val omedaCityPlayerRepository: PlayerRepository,
    private val userRepository: UserRepository
) : UserRecentSearchRepository {

    override suspend fun getRecentSearches(): List<PlayerDetails> {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                return emptyList()
            } else {
                postgrestService.fetchRecentSearches(user.id!!)
                    .map { it.create() }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun addRecentSearch(playerDetails: PlayerDetails) {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                return
            } else {
                val playerSearchDto = PlayerSearchDto(
                    createdAt = Timestamp(System.currentTimeMillis()).toString(),
                    id = user.id!!,
                    playerId = UUID.fromString(playerDetails.playerId),
                    displayName = playerDetails.playerName,
                    region = playerDetails.region,
                    rank = playerDetails.rank,
                    rankTitle = playerDetails.rankDetails.rankText,
                    rankImage = playerDetails.rankDetails.rankText,
                    isRanked = playerDetails.rankDetails != RankDetails.UNRANKED,
                    mmr = playerDetails.mmr?.toFloat(),
                )
                val recentSearches = postgrestService.fetchRecentSearches(user.id!!)

                // If the player is not already in the recent searches, add it
                if (!recentSearches.any { it.playerId == playerSearchDto.playerId }) {
                    // If the table is full, update the oldest search
                    if (recentSearches.size >= TABLE_MAX_ROWS) {
                        postgrestService.updateRecentSearch(
                            userId = user.id!!,
                            recentPlayerId = playerSearchDto.playerId,
                            playerSearchDto
                        )
                    }
                    postgrestService.insertRecentSearch(playerSearchDto)

                } else {
                    val updatedPlayerDetailsResult =
                        omedaCityPlayerRepository.fetchPlayerInfo(playerDetails.playerId)
                    val updatedPlayerDetails = updatedPlayerDetailsResult.getOrThrow()
                    val updatedPlayerId = updatedPlayerDetails.playerDetails?.playerId
                    postgrestService.updateRecentSearch(
                        userId = user.id!!,
                        recentPlayerId = UUID.fromString(updatedPlayerId)
                            ?: playerSearchDto.playerId,
                        playerSearchDto
                    )
                }

            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }

    override suspend fun removeRecentSearch(playerId: String) {
        val user = userRepository.getUser()
        try {
            if (user?.id == null) {
                return
            } else {
                postgrestService.deleteRecentSearch(user.id!!, UUID.fromString(playerId))

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
                postgrestService.deleteAllRecentSearches(user.id!!)
            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }
}