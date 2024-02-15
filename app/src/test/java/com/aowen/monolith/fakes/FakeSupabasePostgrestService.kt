package com.aowen.monolith.fakes

import com.aowen.monolith.data.PlayerSearchDto
import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.fakes.data.fakeExistingPlayerSearchDto
import com.aowen.monolith.fakes.data.fakeNewPlayerSearchDto
import com.aowen.monolith.network.SupabasePostgrestService
import com.aowen.monolith.network.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class FakeSupabasePostgrestService(private val recentSearchStatus: RecentSearchStatus? = null) :
    SupabasePostgrestService {

    var searchCount = MutableStateFlow(5)
    override suspend fun fetchPlayer(userId: String): UserProfile {
        return UserProfile("fake-player-id")
    }

    override suspend fun savePlayer(playerId: String, userId: String) {
        // no-op
    }

    override suspend fun fetchUserInfo(email: String): UserInfo {
        return UserInfo(
            UUID.fromString("addc8bb3-20ad-462a-a9f8-8b32bbf57514")
        )
    }

    override suspend fun fetchRecentSearches(id: UUID): List<PlayerSearchDto> {
        val searchList = mutableListOf<PlayerSearchDto>()
        repeat(10) {
            searchList.add(fakeExistingPlayerSearchDto)
        }
        return when (recentSearchStatus) {
            RecentSearchStatus.ADD -> listOf(fakeNewPlayerSearchDto)
            RecentSearchStatus.FULL -> searchList
            RecentSearchStatus.UPDATE,
            null -> listOf(fakeExistingPlayerSearchDto)
        }
    }

    override suspend fun deleteAllRecentSearches(userId: UUID) {
        searchCount.value = 0
    }

    override suspend fun deleteRecentSearch(userId: UUID, recentPlayerId: UUID) {
        searchCount.value = searchCount.value - 1
    }

    override suspend fun insertRecentSearch(playerSearchDto: PlayerSearchDto) {
        searchCount.value = searchCount.value + 1
    }

    override suspend fun updateRecentSearch(
        userId: UUID,
        recentPlayerId: UUID,
        rankImage: String,
        playerSearchDto: PlayerSearchDto
    ) {
        searchCount.value = searchCount.value + 2
    }
}

enum class RecentSearchStatus {
    ADD,
    UPDATE,
    FULL
}