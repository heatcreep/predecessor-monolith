package com.aowen.predcompanion.core.data.repository.matches

import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchesDetails
import com.aowen.monolith.data.asMatchDetails
import com.aowen.monolith.data.asMatchesDetails
import com.aowen.predcompanion.core.network.OmedaCityService
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
import javax.inject.Inject

class OmedaCityMatchRepository @Inject constructor(
    private val omedaCityService: OmedaCityService
) : MatchRepository {

    override suspend fun fetchMatchesById(
        playerId: String,
        perPage: Int?,
        timeFrame: String?,
        heroId: Int?,
        role: String?,
        playerName: String?,
        page: Int?
    ): Resource<MatchesDetails> =
        safeApiCall(
            apiCall = {
                omedaCityService.getPlayerMatchesById(
                    playerId = playerId,
                    perPage = perPage,
                    timeFrame = timeFrame,
                    heroId = heroId,
                    role = role,
                    playerName = playerName,
                    page = page
                )
            },
            transform = { matchesResponse -> matchesResponse.asMatchesDetails() }
        )

    override suspend fun fetchMatchById(matchId: String): Resource<MatchDetails?> =
        safeApiCall(
            apiCall = { omedaCityService.getMatchById(matchId) },
            transform = { matchResponse -> matchResponse.asMatchDetails() }
        )
}