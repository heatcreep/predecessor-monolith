package com.aowen.predcompanion.core.testing.fakes.service

import com.aowen.predcompanion.core.network.SupabasePostgrestService
import com.aowen.predcompanion.core.network.model.NetworkFavoriteHeroBuild
import com.aowen.predcompanion.core.network.model.NetworkPlayerSearchResult
import com.aowen.predcompanion.core.network.model.NetworkUserInfo
import com.aowen.predcompanion.core.network.model.NetworkUserProfile
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkExistingPlayerSearch
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkNewPlayerSearch
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class FakeSupabasePostgrestService(private val recentSearchStatus: RecentSearchStatus? = null) :
    SupabasePostgrestService {

    var searchCount = MutableStateFlow(5)
    override suspend fun fetchPlayer(userId: String): NetworkUserProfile {
        return NetworkUserProfile("fake-player-id")
    }

    override suspend fun savePlayer(playerId: String, userId: String) {
        // no-op
    }

    override suspend fun fetchUserInfo(email: String): NetworkUserInfo {
        return NetworkUserInfo(
            id = UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57514")
        )
    }

    override suspend fun fetchRecentSearches(id: UUID): List<NetworkPlayerSearchResult> {
        val searchList = mutableListOf<NetworkPlayerSearchResult>()
        repeat(10) {
            searchList.add(fakeNetworkExistingPlayerSearch)
        }
        return when (recentSearchStatus) {
            RecentSearchStatus.ADD -> listOf(fakeNetworkNewPlayerSearch)
            RecentSearchStatus.FULL -> searchList
            RecentSearchStatus.UPDATE,
            null -> listOf(fakeNetworkExistingPlayerSearch)
        }
    }

    override suspend fun deleteAllRecentSearches(userId: UUID) {
        searchCount.value = 0
    }

    override suspend fun deleteRecentSearch(userId: UUID, recentPlayerId: UUID) {
        searchCount.value -= 1
    }

    override suspend fun insertRecentSearch(networkPlayerSearchResult: NetworkPlayerSearchResult) {
        searchCount.value += 1
    }

    override suspend fun updateRecentSearch(
        userId: UUID,
        recentPlayerId: UUID,
        networkPlayerSearchResult: NetworkPlayerSearchResult
    ) {
        searchCount.value += 2
    }

    override suspend fun fetchFavoriteBuilds(userId: UUID): List<NetworkFavoriteHeroBuild> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavoriteBuild(networkFavoriteHeroBuild: NetworkFavoriteHeroBuild) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavoriteBuild(userId: UUID, buildId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllFavoriteBuilds(userId: UUID) {
        TODO("Not yet implemented")
    }
}

enum class RecentSearchStatus {
    ADD,
    UPDATE,
    FULL
}