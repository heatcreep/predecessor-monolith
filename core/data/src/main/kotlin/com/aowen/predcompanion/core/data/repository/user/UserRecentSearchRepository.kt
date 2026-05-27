package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.data.model.asNetworkPlayerSearchResult
import com.aowen.predcompanion.core.data.model.asPlayerDetails
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.SupabasePostgrestService
import com.aowen.predcompanion.core.network.TABLE_MAX_ROWS
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.logDebug
import java.util.UUID
import javax.inject.Inject

interface UserRecentSearchRepository {

    suspend fun getRecentSearches(): List<PlayerInfo.PlayerDetails>
    suspend fun addRecentSearch(playerDetails: PlayerInfo.PlayerDetails)
    suspend fun removeRecentSearch(playerId: String)

    suspend fun removeAllRecentSearches()
}

class NetworkUserRecentSearchRepository @Inject constructor(
    private val postgrestService: SupabasePostgrestService,
    private val omedaCityPlayerRepository: PlayerRepository,
    private val userRepository: UserRepository
) : UserRecentSearchRepository {

    override suspend fun getRecentSearches(): List<PlayerInfo.PlayerDetails> {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                return emptyList()
            } else {
                postgrestService.fetchRecentSearches(user.id!!)
                    .map { it.asPlayerDetails() }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun addRecentSearch(playerDetails: PlayerInfo.PlayerDetails) {
        val user = userRepository.getUser()
        return try {
            if (user?.id == null) {
                return
            } else {
                val networkPlayerSearchResult = playerDetails.asNetworkPlayerSearchResult()
                val recentSearches = postgrestService.fetchRecentSearches(user.id!!)

                // If the player is not already in the recent searches, add it
                if (!recentSearches.any { it.playerId == networkPlayerSearchResult.playerId }) {
                    // If the table is full, update the oldest search
                    if (recentSearches.size >= TABLE_MAX_ROWS) {
                        postgrestService.updateRecentSearch(
                            userId = user.id!!,
                            recentPlayerId = networkPlayerSearchResult.playerId,
                            networkPlayerSearchResult
                        )
                    }
                    postgrestService.insertRecentSearch(networkPlayerSearchResult)

                } else {
                    val updatedPlayerDetailsResult =
                        omedaCityPlayerRepository.fetchPlayerInfo(playerDetails.playerId)
                    val updatedPlayerDetails = updatedPlayerDetailsResult.getOrThrow()
                    val updatedPlayerId = updatedPlayerDetails.playerDetails?.playerId
                    postgrestService.updateRecentSearch(
                        userId = user.id!!,
                        recentPlayerId = UUID.fromString(updatedPlayerId)
                            ?: networkPlayerSearchResult.playerId,
                        networkPlayerSearchResult
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