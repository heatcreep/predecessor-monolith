package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.model.data.MatchesDetails
import com.aowen.predcompanion.core.model.data.asMatchDetails
import com.aowen.predcompanion.core.data.repository.matches.MatchRepository
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkMatch

class FakeOmedaCityMatchRepository : MatchRepository {
    override suspend fun fetchMatchesById(
        playerId: String,
        perPage: Int?,
        timeFrame: String?,
        heroId: Int?,
        role: String?,
        playerName: String?,
        page: Int?
    ): Resource<MatchesDetails> =
        Resource.Success(MatchesDetails(listOf(fakeNetworkMatch.asMatchDetails()), "cursor"))

    override suspend fun fetchMatchById(matchId: String): Resource<MatchDetails?> =
        Resource.Success(fakeNetworkMatch.asMatchDetails())


}