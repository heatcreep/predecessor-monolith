package com.aowen.predcompanion.core.data.repository.matches

import com.aowen.predcompanion.core.model.data.MatchDetails
import com.aowen.predcompanion.core.model.data.MatchesDetails
import com.aowen.predcompanion.core.network.Resource

interface MatchRepository {

    // Matches
    suspend fun fetchMatchesById(
        playerId: String,
        perPage: Int? = null,
        timeFrame: String? = null,
        heroId: Int? = null,
        role: String? = null,
        playerName: String? = null,
        page: Int? = 1
    ): Resource<MatchesDetails>

    suspend fun fetchMatchById(matchId: String): Resource<MatchDetails?>
}