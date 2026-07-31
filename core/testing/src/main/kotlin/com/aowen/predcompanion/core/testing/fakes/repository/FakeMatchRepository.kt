package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.matches.MatchRepository
import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.network.Resource

class FakeMatchRepository : MatchRepository {

    var matchToReturn: Resource<MatchDetails?> = Resource.Success(null)

    override suspend fun fetchMatchById(matchId: String): Resource<MatchDetails?> = matchToReturn
}
