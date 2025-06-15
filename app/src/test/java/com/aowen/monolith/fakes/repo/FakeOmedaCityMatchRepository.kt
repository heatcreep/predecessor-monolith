package com.aowen.monolith.fakes.repo

import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchesDetails
import com.aowen.monolith.data.asMatchDetails
import com.aowen.monolith.data.repository.matches.MatchRepository
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.network.Resource

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