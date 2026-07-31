package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.user.UserRecentSearchRepository
import com.aowen.predcompanion.core.model.data.PlayerInfo

class FakeUserRecentSearchRepository : UserRecentSearchRepository {

    val recentSearches = mutableListOf<PlayerInfo.PlayerDetails>()

    override suspend fun getRecentSearches(): List<PlayerInfo.PlayerDetails> =
        recentSearches.toList()

    override suspend fun addRecentSearch(playerDetails: PlayerInfo.PlayerDetails) {
        recentSearches.add(playerDetails)
    }

    override suspend fun removeRecentSearch(playerId: String) {
        recentSearches.removeAll { it.playerId == playerId }
    }

    override suspend fun removeAllRecentSearches() {
        recentSearches.clear()
    }
}
