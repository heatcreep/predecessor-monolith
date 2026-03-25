package com.aowen.monolith.data.repository.matches

import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchesDetails
import com.aowen.monolith.network.Resource

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