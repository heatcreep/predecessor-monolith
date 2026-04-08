package com.aowen.predcompanion.fakes.repo

import com.aowen.predcompanion.data.MatchDetails
import com.aowen.predcompanion.data.MatchesDetails
import com.aowen.predcompanion.data.asMatchDetails
import com.aowen.predcompanion.data.repository.matches.MatchRepository
import com.aowen.predcompanion.fakes.data.fakeMatchDto
import com.aowen.predcompanion.core.network.Resource

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
        Resource.Success(MatchesDetails(listOf(fakeMatchDto.asMatchDetails()), "cursor"))

    override suspend fun fetchMatchById(matchId: String): Resource<MatchDetails?> =
        Resource.Success(fakeMatchDto.asMatchDetails())


}