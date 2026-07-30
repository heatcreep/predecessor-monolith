package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.model.data.PlayerInfo
import javax.inject.Inject

interface UserRecentSearchRepository {

    suspend fun getRecentSearches(): List<PlayerInfo.PlayerDetails>
    suspend fun addRecentSearch(playerDetails: PlayerInfo.PlayerDetails)
    suspend fun removeRecentSearch(playerId: String)

    suspend fun removeAllRecentSearches()
}

class NetworkUserRecentSearchRepository @Inject constructor() : UserRecentSearchRepository {

    override suspend fun getRecentSearches(): List<PlayerInfo.PlayerDetails> = emptyList()

    override suspend fun addRecentSearch(playerDetails: PlayerInfo.PlayerDetails) {}

    override suspend fun removeRecentSearch(playerId: String) {}

    override suspend fun removeAllRecentSearches() {}
}
