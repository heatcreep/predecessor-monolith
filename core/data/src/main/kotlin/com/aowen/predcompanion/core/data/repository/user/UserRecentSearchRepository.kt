package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.data.model.asNetworkPlayerSearchResult
import com.aowen.predcompanion.core.data.model.asPlayerDetails
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.network.SupabaseAuthService
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
    private val authService: SupabaseAuthService,
) : UserRecentSearchRepository {

    private suspend fun currentUserId(): UUID? =
        authService.currentSession()?.user?.id?.let { UUID.fromString(it) }

    override suspend fun getRecentSearches(): List<PlayerInfo.PlayerDetails> {
        val userId = currentUserId()
        return try {
            if (userId == null) {
                return emptyList()
            } else {
                postgrestService.fetchRecentSearches(userId)
                    .map { it.asPlayerDetails() }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun addRecentSearch(playerDetails: PlayerInfo.PlayerDetails) {
        val userId = currentUserId()
        return try {
            if (userId == null) {
                return
            } else {
                val networkPlayerSearchResult = playerDetails.asNetworkPlayerSearchResult()
                val recentSearches = postgrestService.fetchRecentSearches(userId)

                if (!recentSearches.any { it.playerId == networkPlayerSearchResult.playerId }) {
                    if (recentSearches.size >= TABLE_MAX_ROWS) {
                        postgrestService.updateRecentSearch(
                            userId = userId,
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
                        userId = userId,
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
        val userId = currentUserId()
        try {
            if (userId == null) {
                return
            } else {
                postgrestService.deleteRecentSearch(userId, UUID.fromString(playerId))
            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }

    override suspend fun removeAllRecentSearches() {
        val userId = currentUserId()
        try {
            if (userId == null) {
                return
            } else {
                postgrestService.deleteAllRecentSearches(userId)
            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
    }
}