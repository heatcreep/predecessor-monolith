package com.aowen.monolith.fakes

import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.fakes.data.fakePlayerDetails
import com.aowen.monolith.fakes.data.fakePlayerDetails2
import com.aowen.monolith.network.UserRecentSearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FakeUserRecentSearchesRepository: UserRecentSearchRepository {

    private val _searchCounter: MutableStateFlow<Int> = MutableStateFlow(10)
    val searchCounter: StateFlow<Int> = _searchCounter
    override suspend fun getRecentSearches(): List<PlayerDetails> {
        return listOf(
            fakePlayerDetails,
            fakePlayerDetails2
        )
    }

    override suspend fun addRecentSearch(playerDetails: PlayerDetails) {
        _searchCounter.update { it + 1 }
    }

    override suspend fun removeRecentSearch(playerId: String) {
        _searchCounter.update { it - 1 }
    }

    override suspend fun removeAllRecentSearches() {
        _searchCounter.value = 0
    }
}